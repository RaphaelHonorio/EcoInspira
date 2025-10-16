package com.example.ecoinspira.config.mock

import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.generate.FormaDeReciclar
import com.example.ecoinspira.models.generate.Passo

object EcoGenerateMock {

    fun mockResultado(): EcoGenerateModel {
        return EcoGenerateModel(
            title = "Ideias para reciclar garrafa PET",
            motivo = null,
            formasDeReciclar = listOf(
                FormaDeReciclar(opcao = "Transformar em vaso de planta"),
                FormaDeReciclar(opcao = "Fazer brinquedos recicláveis"),
                FormaDeReciclar(opcao = "Criar organizadores de mesa")
            )
        )
    }

    fun mockPassos(): EcoGenerateStepsModel {
        return EcoGenerateStepsModel(
            material = "Garraga Pet",
            objeto = "Caixa de presente",
            title = "Como transformar caixa de sapato em caixa de presentes",
            passos = listOf(
                Passo(1, "Limpe a caixa de sapato para remover qualquer poeira ou sujeira."),
                Passo(2, "Pinte ou encape a caixa com papel de presente."),
                Passo(3, "Corte uma fita e amarre ao redor da caixa."),
                Passo(4, "Adicione decoração extra, como laços ou etiquetas."),
                Passo(5, "Coloque papel de seda dentro da caixa."),
                Passo(6, "Insira o presente e cubra com mais papel de seda."),
                Passo(7, "Feche a caixa e finalize com um laço bonito.")
            )
        )
    }
}