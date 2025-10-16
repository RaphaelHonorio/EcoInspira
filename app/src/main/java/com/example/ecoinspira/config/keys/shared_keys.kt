package com.example.ecoinspira.config.keys


// --== Chaves gerais compartilhadas
class EcoMemoKeys private constructor() {
    companion object {
        const val token: String = "UIDREFTOKEN"
        const val nome: String = "USERNAME"
    }
}

data class EcoMemoChunks(
    val identidade: String = "AUTHTICKETDEF",
    val default: String = "DEFMEMOTICKET",
    val venda: String = "DEFMEMOVENDA",
    val carrinho: String = "CARTMEMREF",
    val ativacao: String = "ATIVATERMDEF",
    val config: String = "CONFIGTERMINAL",
    val modalidadesPagamento: String = "MODALIDADESPAGAMENTO"
)
