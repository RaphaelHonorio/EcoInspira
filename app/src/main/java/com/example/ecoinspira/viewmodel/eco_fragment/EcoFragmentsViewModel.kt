package com.example.ecoinspira.viewmodel.eco_fragment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoinspira.config.screen.EcoFragmentConfig
import com.example.ecoinspira.config.screen.EcoFragmentSliderView
import com.example.ecoinspira.config.screen.EcoNavbarSliderView
import com.example.ecoinspira.config.screen.EcoTopbarSliderView
import com.example.ecoinspira.config.screen.cadastroProdutoFragmentConfig
import com.example.ecoinspira.config.screen.loginFragmentConfig
import com.example.ecoinspira.config.screen.offsetNavbarVisivel
import com.example.ecoinspira.config.screen.offsetTopBarVisivel

class EcoFragmentsViewModel : ViewModel() {

    // --== Referência da tela atual
    private val telaAnterior = mutableStateOf<EcoFragmentsNavigation?>(null)

    // --== Referência da tela atual
    val telaAtual = MutableLiveData(loginFragmentConfig)

    // --== Manipulador da tela (Fragmento) de Login
    val loginFragmentView = MutableLiveData(EcoFragmentSliderView())

    // --== Manipulador da tela (Fragmento) de Cadastro
    val cadastroFragmentView = MutableLiveData(EcoFragmentSliderView())

    // --== Manipulador da tela (Fragmento) de Testes
    val testeFragmentView = MutableLiveData(EcoFragmentSliderView())

    val escopoDeVisualizacao = MutableLiveData(EcoFragmentsNavigationScope.Login)


    // --== Referência de qual tela responde à qual controlador
    private val refFragmentViewMap = mapOf(
        EcoFragmentsNavigation.Login to loginFragmentView,
        EcoFragmentsNavigation.Cadastro to cadastroFragmentView,
        EcoFragmentsNavigation.Testes to testeFragmentView
    )

    fun verLogin() {
        fecharNavbar()
        fecharTopbar()
        alterarEscopoDeVisualizacao(EcoFragmentsNavigationScope.Login)
        marcarComoAnterior(EcoFragmentsNavigation.Login)
        fecharTodosEAbrir(EcoFragmentsNavigation.Login)
    }



    private fun fecharTodosEAbrir(abrir: EcoFragmentsNavigation) {
        // --== Atualizando referência
        telaAtual.value = dadosDaTela(abrir)

        // --== Mapeando e alterando visibilidade
        refFragmentViewMap.map { (id, fragmentControllerView) ->
            fragmentControllerView.value?.let { fragment ->

                // --== Caso não seja o atual e esteja visivel, então fechar
                if (id != abrir && fragment.estaVisivel)
                    fragmentControllerView.value?.passar()

                // --== Caso não esteja visivel e seja o atual, então abrir
                if (id == abrir && (!(fragment.estaVisivel)))
                    fragmentControllerView.value?.trazer()
            }
        }
    }

    private fun fecharTodos() {

        // --== Mapeando e alterando visibilidade
        refFragmentViewMap.map { (_, fragmentControllerView) ->
            fragmentControllerView.value?.let { fragment ->

                // --== Caso esteja visível, então fechar
                if (fragment.estaVisivel)
                    fragmentControllerView.value?.passar()
            }
        }
    }

    private fun dadosDaTela(tela: EcoFragmentsNavigation): EcoFragmentConfig {
        return when (tela) {

            EcoFragmentsNavigation.Login -> loginFragmentConfig
            EcoFragmentsNavigation.Cadastro -> cadastroProdutoFragmentConfig

            else -> loginFragmentConfig
        }
    }

    private fun marcarComoAnterior(alvo: EcoFragmentsNavigation? = null) {
        telaAnterior.value = alvo
    }


    val topbarView = MutableLiveData(EcoTopbarSliderView())

    val navBarView = MutableLiveData(EcoNavbarSliderView())

    private fun alterarEscopoDeVisualizacao(scope: EcoFragmentsNavigationScope) {
        if (this.escopoDeVisualizacao.value != scope)
            this.escopoDeVisualizacao.value = scope
    }

    fun fecharNavbar() = navBarView.value?.mandarPraBaixo()


    fun fecharTopbar() = topbarView.value?.mandarPraCima()

}




enum class EcoFragmentsNavigation {
    Login, Cadastro, Testes
}

enum class EcoFragmentsNavigationScope {
    Login, Testes
}

