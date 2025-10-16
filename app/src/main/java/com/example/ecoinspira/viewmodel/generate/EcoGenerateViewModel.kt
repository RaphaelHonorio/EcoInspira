package com.example.ecoinspira.viewmodel.generate

import androidx.lifecycle.ViewModel
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.services.generate.IEcoGenerateService

class EcoGenerateViewModel(
    private val ecoGenerateService: IEcoGenerateService
) : ViewModel() {

    var material: String = ""

    fun getGenerateFormValues(): EcoGenerateModel {
        return EcoGenerateModel(material = material)
    }
}