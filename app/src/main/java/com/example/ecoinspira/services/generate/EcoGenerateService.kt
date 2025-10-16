package com.example.ecoinspira.services.generate

import android.content.Context
import com.example.ecoinspira.extensions.http.serializeAndResolve
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.services.httpclient.HttpClient

class EcoGenerateService : IEcoGenerateService {

    private var _httpClient: HttpClient = HttpClient("https://10.0.2.2:7090")

    override suspend fun gerar(
        context: Context,
        model: EcoGenerateModel,
        options: EcoAPICallback<EcoGenerateModel>
    ) {
        _httpClient.postAsync<EcoGenerateModel>(context = context, path = "post/generate", payload = model, auth= true, EcoGenerateModel::class.java).serializeAndResolve(options)
    }

    override suspend fun passos(
        context: Context,
        model: EcoGenerateStepsModel,
        options: EcoAPICallback<EcoGenerateStepsModel>
    ) {
        _httpClient.postAsync<EcoGenerateStepsModel>(context= context, path = "post/generate/steps", payload = model, auth = true, EcoGenerateStepsModel::class.java).serializeAndResolve(options)
    }
}