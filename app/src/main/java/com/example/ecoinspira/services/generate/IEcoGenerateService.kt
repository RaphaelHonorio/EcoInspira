package com.example.ecoinspira.services.generate

import android.content.Context
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.http.EcoAPICallback
interface IEcoGenerateService {
    suspend fun gerar(
        context: Context,
        model: EcoGenerateModel,
        options: EcoAPICallback<EcoGenerateModel>
    )

    suspend fun passos(
        context: Context,
        model: EcoGenerateStepsModel,
        options: EcoAPICallback<EcoGenerateStepsModel>
    )
}