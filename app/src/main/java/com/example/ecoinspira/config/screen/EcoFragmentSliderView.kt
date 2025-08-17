package com.example.ecoinspira.config.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf

class EcoFragmentSliderView(
    val offSet: MutableState<Float> = mutableFloatStateOf(offsetEscondidoADireita),
    val targetOffSet: MutableState<Float> = mutableFloatStateOf(offsetEscondidoADireita),
    ) {

    fun passar() {
        this.offSet.value = offsetVisivel
        this.targetOffSet.value = offsetEscondidoADireita
        this.offSet.value = offsetEscondidoADireita
    }

    fun trazer() {
        this.offSet.value = offsetEscondidoADireita
        this.targetOffSet.value = offsetVisivel
    }

    val estaVisivel: Boolean get() = targetOffSet.value == offsetVisivel
}

data class EcoTopbarSliderView(
    val offSet: MutableState<Float> = mutableFloatStateOf(offsetTopBarEscondidoPraCima),
    val targetOffSet: MutableState<Float> = mutableFloatStateOf(offsetTopBarEscondidoPraCima),
) {
    fun mandarPraCima() {
        this.offSet.value = offsetTopBarVisivel
        this.targetOffSet.value = offsetTopBarEscondidoPraCima
    }

    fun puxarPraBaixo() {
        this.offSet.value = offsetTopBarEscondidoPraCima
        this.targetOffSet.value = offsetTopBarVisivel
    }
}

data class EcoNavbarSliderView(
    val offSet: MutableState<Float> = mutableFloatStateOf(offsetNavbarEscondidoPraBaixo),
    val targetOffSet: MutableState<Float> = mutableFloatStateOf(offsetNavbarEscondidoPraBaixo),
) {
    fun puxarPraCima() {
        this.offSet.value = offsetNavbarEscondidoPraBaixo
        this.targetOffSet.value = offsetNavbarVisivel
    }

    fun mandarPraBaixo() {
        this.offSet.value = offsetNavbarVisivel
        this.targetOffSet.value = offsetNavbarEscondidoPraBaixo
    }
}




// --== Valor de referência para conteúdo escondido à direta
const val offsetEscondidoADireita = 500f

// --== Valor de referência para conteúdo visível
const val offsetVisivel = 0f

// --==
const val offsetTopBarEscondidoPraCima = -70f

// --== Valor de referência para conteúdo visível de topbar
const val offsetTopBarVisivel = 0f

// --
const val offsetNavbarEscondidoPraBaixo = 700f

// --== Valor de referência para conteúdo visível de navbar
const val offsetNavbarVisivel = 565f