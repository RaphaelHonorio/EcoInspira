package com.example.ecoinspira.models.user

data class EcoUserModel(
    var name: String? = "Nome Teste no model",
    var email: String? = "Email Teste no model",
    var password: String? ="Senha teste no model",
    var cpf: String? = "12312312312",
    var dataNascimento: String? = "05040202",
    var tokens: Tokens? = null
)


data class Tokens(
    var accessToken: String? = null
)