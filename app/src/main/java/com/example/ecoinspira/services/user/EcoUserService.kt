package com.example.ecoinspira.services.user

import android.content.Context
import com.example.ecoinspira.extensions.http.serializeAndResolve
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.user.EcoUserModel
import com.example.ecoinspira.services.httpclient.HttpClient

class EcoUserService : IEcoUserService {

    private var _httpClient: HttpClient = HttpClient("https://10.0.2.2:7090")

    override suspend fun cadastrar(
        context: Context,
        model: EcoUserModel,
        options: EcoAPICallback<EcoUserModel>
    ) {
        _httpClient.postAsync<EcoUserModel>(context = context, path = "user", payload = model, auth = false, EcoUserModel::class.java).serializeAndResolve(options)
    }

    override suspend fun logar(
        context: Context,
        model: EcoUserModel,
        options: EcoAPICallback<EcoUserModel>
    ) {
        _httpClient.postAsync<EcoUserModel>(context = context, path = "login", payload = model, auth = false, EcoUserModel::class.java).serializeAndResolve(options)
    }
}