package com.example.ecoinspira.config.screen

import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsNavigation


data class EcoFragmentConfig(
    val id : EcoFragmentsNavigation,
    val title : String,
    val onBack : () -> Unit
)

// --== Opções de tela de produtos
val loginFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Login,
    title = "Grade de produtos",
    onBack = { }
)

// --== Opções de tela de produtos
val cadastroProdutoFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Cadastro,
    title = "Voltar",
    onBack = { }
)
