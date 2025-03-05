package com.example.ecoinspira.services.hugging_face

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HuggingFaceApi {
    @POST("models/facebook/detr-resnet-101")
    fun analyzeImage(
        @Header("Authorization") token: String,
        @Body request: HuggingFaceRequest
    ): Call<List<Prediction>> // Agora espera uma lista de Prediction
}

data class HuggingFaceRequest(
    val inputs: String // Imagem em base64
)

data class Prediction(
    val label: String, // Nome do objeto detectado
    val score: Double, // Confiança da detecção (0 a 1)
    val box: Box? // Coordenadas da caixa delimitadora (opcional)
)

data class Box(
    val xmin: Int,
    val ymin: Int,
    val xmax: Int,
    val ymax: Int
)