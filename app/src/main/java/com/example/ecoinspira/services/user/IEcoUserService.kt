package com.example.ecoinspira.services.user

import android.content.Context
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.user.EcoUserModel

interface IEcoUserService {
    suspend fun cadastrar(
        context: Context,
        model: EcoUserModel,
        options: EcoAPICallback<EcoUserModel>
        )
}