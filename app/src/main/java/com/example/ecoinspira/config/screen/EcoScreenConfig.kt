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

// --== Opções de tela de Cadastro
val cadastroFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Cadastro,
    title = "Voltar",
    onBack = { }
)

// --== Opções de tela de Cadastro
val feedFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Feed,
    title = "Voltar",
    onBack = { }
)


// --== Opções de tela de Cadastro
val perfilFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Perfil,
    title = "Voltar",
    onBack = { }
)

// --== Opções de tela de Cadastro
val configFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Config,
    title = "Voltar",
    onBack = { }
)

// --== Opções de tela de Cadastro
val friendsFragmentConfig = EcoFragmentConfig(
    id = EcoFragmentsNavigation.Analysis,
    title = "Voltar",
    onBack = { }
)