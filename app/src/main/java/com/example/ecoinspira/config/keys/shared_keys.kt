package com.example.ecoinspira.config.keys


// --== Chaves gerais compartilhadas
class EcoMemoKeys private constructor() {
    companion object {
        const val token: String = "UIDREFTOKEN"
        const val modalidade: String = "TERMINALCONFIG"
        const val modalidadesPagamento: String = "TERMINALCONFIGPAGAMENTO"
        const val modoTerminalAtivacao: String = "MDATIVATERMINAL-"
        const val terminalId: String = "TERMIDLOCAL-"
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

val EcoMemoChunksData = EcoMemoChunks()