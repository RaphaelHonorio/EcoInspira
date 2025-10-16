package com.example.ecoinspira.models.post

data class EcoPostModel(
    val title: String,
    val description: String,
    val likesCount: Long,
    val commentsCount: Long,
    val imageUrl: String? = null // retornado no GET
)