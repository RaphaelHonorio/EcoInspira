package com.example.ecoinspira.models.generate

data class EcoGenerateModel(
  var material: String? = null, // usado no envio
  var title: String? = null, // usado na resposta
  var motivo: String? = null, // usado na resposta
  var formasDeReciclar: List<FormaDeReciclar>? = null // usado na resposta
)

data class FormaDeReciclar(
  val opcao: String
)

data class EcoGenerateStepsModel(

  // --- Envio (request) ---
  val material: String,
  val objeto: String,

  // --- Retorno (response) ---
  val title: String? = null,
  val passos: List<Passo>? = null
)

data class Passo(
  val ordem: Int,
  val descricao: String
)