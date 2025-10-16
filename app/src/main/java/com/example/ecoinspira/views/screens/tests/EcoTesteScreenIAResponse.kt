package com.example.ecoinspira.views.screens.tests

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecoinspira.extensions.activity.EcoActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

class EcoTesteScreenIAResponse : EcoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ChatAIScreen()
            }
        }
    }
}

@Composable
fun ChatAIScreen() {
    val viewModel: ChatAIViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var messageText by remember { mutableStateOf("") }
    // Substitua pelo seu token do Hugging Face
    val apiKey = "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 "

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Lista de mensagens
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatMessageBubble(
                    message = message,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // Campo de entrada e botão
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Digite sua mensagem") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.sendMessage(messageText, apiKey)
                    messageText = ""
                },
                enabled = messageText.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enviar")
                }
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val colors = if (message.isFromUser) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(colors = colors) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = if (message.isFromUser) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

class ChatAIViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val apiService = HuggingFaceApiService.create()

    fun sendMessage(message: String, apiKey: String) {
        if (message.isBlank()) return

        _isLoading.value = true
        _error.value = null

        _messages.value = _messages.value + ChatMessage(
            text = message,
            isFromUser = true
        )

        viewModelScope.launch {
            try {
                val response = apiService.generateText(
                    auth = "Bearer $apiKey",
                    request = HuggingFaceRequest(
                        inputs = message,
                        parameters = Parameters(
                            max_new_tokens = 60,
                            return_full_text = false
                        )
                    )
                )

                if (response.isNotEmpty()) {
                    _messages.value = _messages.value + ChatMessage(
                        text = response[0].generated_text,
                        isFromUser = false
                    )
                } else {
                    _error.value = "Nenhuma resposta recebida da API"
                }
            } catch (e: Exception) {
                _error.value = "Erro: ${e.message ?: "Erro desconhecido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

// Modelos de dados para a API Hugging Face
data class HuggingFaceRequest(
    @SerializedName("inputs") val inputs: String,
    @SerializedName("parameters") val parameters: Parameters? = null
)

data class Parameters(
    @SerializedName("max_new_tokens") val max_new_tokens: Int? = null,
    @SerializedName("return_full_text") val return_full_text: Boolean? = null
)

data class HuggingFaceResponse(
    @SerializedName("generated_text") val generated_text: String
)

interface HuggingFaceApiService {
    @POST("models/microsoft/DialoGPT-medium")
    suspend fun generateText(
        @Header("Authorization") auth: String,
        @Body request: HuggingFaceRequest
    ): List<HuggingFaceResponse>

    companion object {
        fun create(): HuggingFaceApiService {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api-inference.huggingface.co/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HuggingFaceApiService::class.java)
        }
    }
}