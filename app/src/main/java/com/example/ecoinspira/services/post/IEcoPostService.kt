package com.example.ecoinspira.services.post

import android.content.Context
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.models.post.GetPostResponse
import java.io.File

interface IEcoPostService {
    suspend fun uploadPost(
        context: Context,
        post: EcoPostModel,
        imageFile: File,
        options: EcoAPICallback<EcoPostModel>
    )

    suspend fun getAllPosts(
        context: Context,
        options: EcoAPICallback<GetPostResponse>
    )
}