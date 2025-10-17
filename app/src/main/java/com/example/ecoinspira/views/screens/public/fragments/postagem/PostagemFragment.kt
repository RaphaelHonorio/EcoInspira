package com.example.ecoinspira.views.screens.public.fragments.postagem


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.config.mock.EcoGenerateMock.mockPassos
import com.example.ecoinspira.config.mock.EcoGenerateMock.mockResultado
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.generate.FormaDeReciclar
import com.example.ecoinspira.models.generate.Passo
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.services.generate.IEcoGenerateService
import com.example.ecoinspira.services.post.IEcoPostService
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.generate.EcoGenerateViewModel
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnrememberedMutableState")
@Composable
fun PostagemFragment(
    fragmentMainViewModel: EcoFragmentsViewModel,
    generateViewModel: EcoGenerateViewModel
) {
    val context = LocalContext.current
    val generateService: IEcoGenerateService = get()

    val materialInput = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf<String?>(null) }

    val resultado = remember { mutableStateOf<EcoGenerateModel?>(null) }
    val passosResultado = remember { mutableStateOf<EcoGenerateStepsModel?>(null) }

    // mocks
    //val mockResultado = mockResultado()
    //val mockPassos = mockPassos()

    val currentScreen = remember { mutableStateOf("analise") }

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


    EcoFragmentSlider(form = fragmentMainViewModel.analysisFragmentView.observeAsState()) {
        EcoMargin(marginTop = 24.dp) {

            EcoTypography(text = "TESTE DE ANALISE DE OBJETO CHAT GPT", size = 14.sp)


            // campo de texto
            EcoMinimalTextField(
                refValue = materialInput,
                placeholder = "Digite o material",
                imeAction = ImeAction.Done,
                onValueChange = {
                    materialInput.value = it
                    generateViewModel.material = it
                }
            )

            EcoSimpleButton("Gerar Analise", onClick = { handleGerarAnalise() })

            EcoSimpleButton("Gerar Passos", onClick = { handleGerarPasos() })


            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .verticalScroll(rememberScrollState())
            ) {

                when (currentScreen.value) {
                    "analise" -> AnaliseMaterialSection(
                        resultado = resultado.value,
                        selectedOption = selectedOption,
                        onAvancar = { currentScreen.value = "passos" }
                    )

                    "passos" -> PassosReciclagemSection(
                        passosData = passosResultado.value,
                        onVoltar = { currentScreen.value = "analise" }
                    )
                }
            }
        }
    }
}


@Composable
fun AnaliseMaterialSection(
    resultado: EcoGenerateModel?,
    selectedOption: MutableState<String?>,
    onAvancar: () -> Unit
) {
    resultado?.let { res ->
        EcoTypography(text = res.title ?: "Análise do Material")

        if (!res.motivo.isNullOrEmpty()) {
            EcoTypography(text = "Motivo: ${res.motivo}")
        }

        res.formasDeReciclar?.takeIf { it.isNotEmpty() }?.let { formas ->
            Spacer(modifier = Modifier.height(8.dp))
            EcoTypography(text = "Formas de Reciclar:")
            FormaDeReciclarSelector(formas, selectedOption)
        }

        selectedOption.value?.let {
            Spacer(modifier = Modifier.height(8.dp))
            EcoTypography(text = "Opção selecionada: $it")

            // ✅ botão para ir para a próxima etapa (passos)
            Spacer(modifier = Modifier.height(12.dp))
            EcoSimpleButton("Ver Passos", onClick = onAvancar)
        }
    }
}

@Composable
fun PassosReciclagemSection(
    passosData: EcoGenerateStepsModel?,
    onVoltar: () -> Unit
) {
    passosData?.passos?.let { passos ->
        EcoTypography(text = "Passos para criar: ${passosData.title ?: ""}")
        Spacer(modifier = Modifier.height(8.dp))

        passos.forEach { passo ->
            PassoItem(passo)
        }

        Spacer(modifier = Modifier.height(16.dp))
        EcoSimpleButton("Voltar", onClick = onVoltar)
        Spacer(modifier = Modifier.height(16.dp))
        EcoSimpleButton("Continuar", onClick = onVoltar)
    }
}


@Composable
fun FormaDeReciclarSelector(
    formas: List<FormaDeReciclar>,
    selectedOption: MutableState<String?> // <- variável externa
) {
    val selected = remember { mutableStateOf<FormaDeReciclar?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        formas.forEach { forma ->
            val isSelected = forma == selected.value

            Button(
                onClick = {
                    selected.value = forma
                    selectedOption.value = forma.opcao // ✅ salva o texto da opção
                },
                colors = ButtonDefaults.buttonColors(

                    contentColor = if (isSelected) theme.colors.white else theme.colors.black01
                ),
                shape = RoundedCornerShape(12.dp),

                ) {
                Text(
                    text = forma.opcao,
                )
            }
        }
    }
}


@Composable
fun PassoItem(passo: Passo) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier

            .padding(vertical = 6.dp)
            .background(theme.colors.cinza02, RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        // Cabeçalho (apenas número do passo)
        Text(
            text = "Passo ${passo.ordem + 1}",

            )

        // Quando clicado, mostra o texto
        if (expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = passo.descricao,

                )
        }
    }
}
