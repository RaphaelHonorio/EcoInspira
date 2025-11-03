package com.example.ecoinspira.views.screens.public.fragments.postagem


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.config.mock.EcoGenerateMock.mockPassos
import com.example.ecoinspira.config.mock.EcoGenerateMock.mockResultado
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.services.generate.IEcoGenerateService
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.generate.EcoGenerateViewModel
import com.example.ecoinspira.views.components.eco_buttons.EcoMaterialSelectionButton
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_buttons.EcoVoltarButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_passos.PassosReciclagemSection
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import com.example.ecoinspira.views.screens.public.fragments.postagem.steps.Step1
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun PostagemFinalFragment(
    fragmentMainViewModel: EcoFragmentsViewModel, generateViewModel: EcoGenerateViewModel
) {
    val context = LocalContext.current
    val generateService: IEcoGenerateService = get()

    val materialInput = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf<String?>(null) }

    val resultado = remember { mutableStateOf<EcoGenerateModel?>(null) }
    val passosResultado = remember { mutableStateOf<EcoGenerateStepsModel?>(null) }


    val currentScreen = remember { mutableStateOf("analise") }


    // Controle de carregamento
    val isLoading = remember { mutableStateOf(false) }

    // --- Handle gerar análise
    fun handleGerarAnalise() {
        GlobalScope.launch {
            generateService.gerar(
                context = context,
                model = generateViewModel.getGenerateFormValues(),
                options = EcoAPICallback(
                    onSucess = { response ->
                        resultado.value = response
                        currentScreen.value = "analise"
                    },
                    onFailure = { error ->
                        resultado.value = EcoGenerateModel(
                            title = "Erro",
                            motivo = error,
                            formasDeReciclar = emptyList()
                        )
                    }
                )
            )
        }
    }

    // --- Handle gerar passos
    fun handleGerarPasos() {
        val request = EcoGenerateStepsModel(
            material = materialInput.value,
            objeto = selectedOption.value ?: ""
        )

        GlobalScope.launch {
            generateService.passos(
                context = context,
                model = request,
                options = EcoAPICallback(
                    onSucess = { response ->
                        passosResultado.value = response
                        currentScreen.value = "passos"
                    },
                    onFailure = { error ->
                        println("Erro ao gerar passos: $error")
                    }
                )
            )
        }
    }


    EcoFragmentSlider(form = fragmentMainViewModel.postagemFragmentView.observeAsState()) {

        if (!isLoading.value) {
            EcoMargin(marginTop = 32.dp, marginBottom = 16.dp) {
                Column(Modifier.fillMaxWidth(), Arrangement.Center) {
                    EcoTypography(
                        text = "Vamos fazer uma Postagem?",
                        size = 30.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp,
                        weight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row {
                        EcoSimpleButton("Abrir Camera", fullWidth = false, widthFloat = 0.50f)
                        Spacer(Modifier.width(8.dp))
                        EcoSimpleButton("Abrir Galeria")
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        EcoVoltarButton(onClick = { isLoading.value = true })
                    }
                }
            }
        } else {
            fragmentMainViewModel.iniciarCarregamento(
                "Fazendo a Analise da Imagem",
                "Por gentiliza aguarde um pouco enquanto a imagem é analizada"
            ) { fragmentMainViewModel.pararCarregamento(); isLoading.value = false }
        }
    }
}