package com.example.ecoinspira.viewmodel.user

import androidx.lifecycle.ViewModel
import com.example.ecoinspira.models.user.EcoUserModel
import com.example.ecoinspira.services.user.IEcoUserService

class EcoUserViewModel(
    private val ecoUserService: IEcoUserService
): ViewModel() {

    var name: String = ""
    var email: String = ""
    var password: String = ""
    var cpf: String = ""
    var dataNasc: String = ""

    fun getUserFormValues(): EcoUserModel {
        return EcoUserModel(
            name = name,
            email = email,
            password = password,
            cpf = cpf,
            dataNascimento = dataNasc
        )
    }
}