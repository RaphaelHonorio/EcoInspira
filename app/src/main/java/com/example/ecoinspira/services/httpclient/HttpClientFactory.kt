package com.example.ecoinspira.services.httpclient

import android.annotation.SuppressLint
import android.content.Context
import com.example.ecoinspira.config.keys.EcoMemoChunks
import com.example.ecoinspira.config.keys.EcoMemoKeys
import com.example.ecoinspira.models.http.HttpResponseMessage
import com.example.ecoinspira.models.http.ValidationProblemDetails

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.Continuation


// --== Mensagem padrão de erro
const val SERVER_ERROR_MESSAGE =
    "O Servidor não conseguiu processar sua requisção, por favor, tente novamente mais tarde :/"

class HttpClientFactory(
    private val secureOnly: Boolean = false,
) {
    // --== Serializador json
    private val _serializer: Gson = Gson()

    // -- Responsável por criar um novo agente http
    fun createClient(): OkHttpClient {
        var builder = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        // --== Caso o modo de segurança esteja desabilitado, iremos realizar as requisições sem ssl
        if (!secureOnly) builder = builder
            .sslSocketFactory(SSLContext.getInstance("TLS").apply {
                init(null, trustAllCerts, SecureRandom())
            }.socketFactory, trustAllCerts[0] as X509TrustManager)

        // --== Finalizando o build e retornando client
        return builder.hostnameVerifier { _, _ -> true }
            .connectTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .build()
    }

    private fun authHandler(
        auth: Boolean,
        context: Context,
        httpRequestMessage: Request.Builder,
    ): Request {
        if (auth) {
            val sharedPreferences =
                context.getSharedPreferences(EcoMemoChunks().identidade, Context.MODE_PRIVATE)
            val token = sharedPreferences.getString(EcoMemoKeys.token, null)
            httpRequestMessage.addHeader("Authorization", "Bearer $token")
        }
        return httpRequestMessage.build()
    }

    suspend fun <D> postFactoryAsync(
        path: String, body: Any,
        dataType: Type, authorize: Boolean = false, requester: OkHttpClient, context: Context,
    ): HttpResponseMessage<D> = withContext(Dispatchers.IO) {

        // ==-- Criando mensagem de requisição
        val requestMessage = Request.Builder().url(path)
            .post(_serializer.toJson(body).toRequestBody("application/json".toMediaTypeOrNull()))

        // --== Enviando mensagem
        return@withContext sendAsync(
            request = authHandler(authorize, context, requestMessage),
            dataType = dataType, authorize = authorize, context = context, requester = requester
        )
    }

    suspend fun <D> getFactoryAsync(
        path: String,
        dataType: Type, authorize: Boolean = false,
        requester: OkHttpClient, context: Context,
    ): HttpResponseMessage<D> = withContext(Dispatchers.IO) {
        val requestMessage = Request.Builder().url(path).get()
        return@withContext sendAsync(
            request = authHandler(authorize, context, requestMessage),
            dataType = dataType, authorize = authorize, context = context, requester = requester
        )
    }

    suspend fun <D> patchFactoryAsync(
        path: String, body: Any,
        dataType: Type, authorize: Boolean = false,
        requester: OkHttpClient, context: Context,
    ): HttpResponseMessage<D> = withContext(Dispatchers.IO) {
        val requestMessage = Request.Builder().url(path)
            .patch(_serializer.toJson(body).toRequestBody("application/json".toMediaTypeOrNull()))
        return@withContext sendAsync(
            request = authHandler(authorize, context, requestMessage),
            dataType = dataType, authorize = authorize, context = context, requester = requester
        )
    }

    suspend fun <D> putFactoryAsync(
        path: String, body: Any,
        dataType: Type, authorize: Boolean = false,
        requester: OkHttpClient, context: Context,
    ): HttpResponseMessage<D> = withContext(Dispatchers.IO) {
        val requestMessage = Request.Builder().url(path)
            .put(_serializer.toJson(body).toRequestBody("application/json".toMediaTypeOrNull()))
        return@withContext sendAsync(
            request = authHandler(authorize, context, requestMessage),
            dataType = dataType, authorize = authorize, context = context, requester = requester
        )
    }

    suspend fun <D> deleteFactoryAsync(
        path: String, dataType: Type,
        authorize: Boolean = false, requester: OkHttpClient, context: Context,
    ): HttpResponseMessage<D> = withContext(Dispatchers.IO) {
        val requestMessage = Request.Builder().url(path).delete()
        return@withContext sendAsync(
            request = authHandler(authorize, context, requestMessage),
            dataType = dataType, authorize = authorize, context = context, requester = requester
        )
    }

    private suspend fun <D> sendAsync(
        request: Request, dataType: Type,
        authorize: Boolean, context: Context, requester: OkHttpClient,
    ): HttpResponseMessage<D> = suspendCancellableCoroutine { continuation ->
        try {
            requester.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    runBlocking {
                        when {
                            response.isSuccessful -> {
                                val responseBody = response.body.string()
                                val data = _serializer.fromJson<D>(responseBody, dataType)
                                val result = buildHttpResponse(data, response.code, true, null)
                                continuation.resumeWith(Result.success(result))
                            }

                            response.code == 307 -> {
                                val result = handleRedirect<D>(
                                    response,
                                    authorize,
                                    dataType,
                                    context,
                                    requester
                                )
                                continuation.resumeWith(Result.success(result))
                            }

                            else -> handleFailure(response, continuation)
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWith(Result.failure(e))
                }
            })
        } catch (e: Exception) {
            continuation.resumeWith(Result.failure(e))
        }
    }

    private fun <D> buildHttpResponse(
        data: D?, code: Int, success: Boolean,
        trace: ValidationProblemDetails?, stack: String? = null,
    ): HttpResponseMessage<D> =
        HttpResponseMessage(data, code, success, trace, stack ?: SERVER_ERROR_MESSAGE)

    private suspend fun <D> handleRedirect(
        response: Response, authorize: Boolean,
        dataType: Type, context: Context, requester: OkHttpClient,
    ): HttpResponseMessage<D> {
        val redirectRequest = Request.Builder().url(
            response.header("Location") ?: return buildHttpResponse(null, 500, false, null)
        )

        return sendAsync(
            authHandler(authorize, context, redirectRequest), dataType = dataType,
            context = context, authorize = authorize, requester = requester
        )
    }

    private fun <D> handleFailure(
        response: Response,
        continuation: Continuation<HttpResponseMessage<D>>,
    ) {
        val trace = _serializer.fromJson(
            response.body.string()
            , ValidationProblemDetails::class.java
        )

        val httpResponseMessage = buildHttpResponse<D>(null, response.code, false, trace,
            trace?.errors?.values?.flatMap { it.toList() }?.firstOrNull()
        )

        continuation.resumeWith(Result.success(httpResponseMessage))
    }
}

// --== Objeto de configuração de verificação de segurança
val trustAllCerts =
    arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager") object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    })


