package com.example.ecoinspira.services.post

import android.content.Context
import com.example.ecoinspira.extensions.http.serializeAndResolve
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.services.httpclient.HttpClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EcoPostService : IEcoPostService {

    private var _httpClient: HttpClient = HttpClient("https://10.0.2.2:7090")

    override suspend fun uploadPost(
        context: Context,
        post: EcoPostModel,
        imageFile: File,
        options: EcoAPICallback<EcoPostModel>
    ) {
        val fields = mapOf(
            "Title" to post.title,
            "Description" to post.description,
            "LikesCount" to post.likesCount.toString(),
            "CommentsCount" to post.commentsCount.toString()
        )

        // Aqui usamos a função do HttpClientFactory
        _httpClient.postMultipartAsync<EcoPostModel>(
            context = context,
            path = "post",
            fields = fields,
            fileField = "Image",
            file = imageFile,
            dataType = EcoPostModel::class.java,
            auth = true
        ).serializeAndResolve(options)
    }

    override suspend fun getAllPosts(
        context: Context,
        options: EcoAPICallback<List<EcoPostModel>>
    ) {
        _httpClient.getAsync<List<EcoPostModel>>(
            context = context,
            path = "post/upload",
            auth = true,
            dataType = object : TypeToken<List<EcoPostModel>>() {}.type
        ).serializeAndResolve(options)
    }
}