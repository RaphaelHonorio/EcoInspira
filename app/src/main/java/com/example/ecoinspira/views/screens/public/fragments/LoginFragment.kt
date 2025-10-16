package com.example.ecoinspira.views.screens.public.fragments

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.config.keys.EcoMemoChunks
import com.example.ecoinspira.config.keys.EcoMemoKeys
import com.example.ecoinspira.config.others.cpfVisualTransformation
import com.example.ecoinspira.config.others.isValidCPF
import com.example.ecoinspira.extensions.navigation.EcoScreens
import com.example.ecoinspira.extensions.navigation.navigate
import com.example.ecoinspira.extensions.utils.ecoSnackbar
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.services.generate.IEcoGenerateService
import com.example.ecoinspira.services.memo.IEcoMemo
import com.example.ecoinspira.services.user.IEcoUserService
import com.example.ecoinspira.viewmodel.generate.EcoGenerateViewModel
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_buttons.EcoCheckbox
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import com.example.ecoinspira.views.screens.public.EcoMainScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginFragment(
    userViewModel: EcoUserViewModel,
    memoService: IEcoMemo
) {


    var estado by remember { mutableStateOf(false) }

    var email = mutableStateOf("")
    var senha = mutableStateOf("")
    var nome = mutableStateOf("")
    var emailCad = mutableStateOf("")
    var senhaCad = mutableStateOf("")
    var cpf = mutableStateOf("")
    var dataNasc = mutableStateOf("")

    // --== Criar FocusRequesters para cada campo
    val focusNome = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val focusSenha = remember { FocusRequester() }
    val focusCpf = remember { FocusRequester() }
    val focusDataNasc = remember { FocusRequester() }

    var cpfError by remember { mutableStateOf(false) }
    var cpfErrorMessage by remember { mutableStateOf("") }


    // --== REALIZANDO O CADASTRO ==-- \\
    val userService: IEcoUserService = get()
    val context = LocalContext.current

    fun handleSubmit() {
        GlobalScope.launch {
            userService.cadastrar(
                context = context,
                model = userViewModel.getUserFormValues(),
                options = EcoAPICallback(
                    onSucess = {user ->

                        user.tokens?.accessToken?.let {
                            memoService.save(
                                context, EcoMemoKeys.token, it,
                                EcoMemoChunks().identidade
                            )
                        }

                        user.name?.let {
                            memoService.save(context, EcoMemoKeys.nome,
                                it, EcoMemoChunks().identidade)
                        }

                        ecoSnackbar(context, "Cadastro feito com sucesso", color = android.graphics.Color.CYAN)
                        context.navigate(EcoScreens.Main)
                    },
                    onFailure = { error ->
                        println(error)
                        ecoSnackbar(context, error)
                    }
                )
            )
        }
    }

    fun handleLoginSubmit() {
        GlobalScope.launch {
            userService.logar(
                context = context,
                model = userViewModel.getUserFormValues(),
                options = EcoAPICallback(
                    onSucess = {user ->

                        user.tokens?.accessToken?.let {
                            memoService.save(
                                context, EcoMemoKeys.token, it,
                                EcoMemoChunks().identidade
                            )
                        }



                        ecoSnackbar(context, "Login Feito com sucesso", color = android.graphics.Color.CYAN)
                        context.navigate(EcoScreens.Main)

                    },
                    onFailure = { error ->
                        println(error)
                        ecoSnackbar(context, error)
                    }
                )
            )
        }
    }





    var lembrar by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(theme.colors.black01),
        Arrangement.Bottom,
        Alignment.CenterHorizontally
    ) {

        EcoTypography(
            text = "EcoInspira",
            size = 50.sp,
            color = theme.colors.logo,
            weight = FontWeight.Bold
        )

        Spacer(Modifier.size(72.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .height(600.dp)
                .background(
                    theme.colors.white,
                    shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp)
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            EcoMargin(marginBottom = 40.dp, marginTop = 40.dp) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ) {


                    if (!estado) {

                        EcoTypography(
                            text = "Acesse sua conta",
                            size = 24.sp,
                            weight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(16.dp))


                        EcoMinimalTextField(cpf, placeholder = "Cpf", onValueChange = {
                            cpf.value = it
                            userViewModel.cpf = it
                        })
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(senha, placeholder = "senha", onValueChange = {
                            senha.value = it
                            userViewModel.password = it
                        })

                        Spacer(Modifier.size(16.dp))

                        Row(modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            lembrar = !lembrar
                        }) {

                            EcoCheckbox(checked = lembrar, onCheckedChange = { lembrar = it })

                            Spacer(modifier = Modifier.size(8.dp))
                            EcoTypography(text = "Lembre-me", color = theme.colors.cinza02)
                        }

                        Spacer(Modifier.size(32.dp))


                        EcoSimpleButton(onClick = {
                            handleLoginSubmit()
                        }, text = "Login")


                        Spacer(Modifier.size(8.dp))

                        EcoTypography(text = "Ou então cadastre-se", color = theme.colors.cinza02)

                        Spacer(Modifier.size(8.dp))


                    } else {

                        EcoTypography(text = "Cadastro", size = 24.sp, weight = FontWeight.Bold)
                        Spacer(Modifier.size(16.dp))
                        EcoMinimalTextField(nome,
                            placeholder = "nome completp",
                            focusRequester = focusNome,
                            imeAction = ImeAction.Next,
                            onDone = { focusEmail.requestFocus() },
                            onValueChange = {
                                nome.value = it
                                userViewModel.name = it
                            })

                        EcoMinimalTextField(emailCad,
                            placeholder = "email",
                            focusRequester = focusNome,
                            imeAction = ImeAction.Next,
                            onDone = { focusSenha.requestFocus() },
                            onValueChange = {
                                email.value = it
                                userViewModel.email = it
                            })


                        EcoMinimalTextField(senhaCad,
                            placeholder = "senha",
                            focusRequester = focusNome,
                            imeAction = ImeAction.Next,
                            visualTransformation = PasswordVisualTransformation(),
                            onDone = { focusCpf.requestFocus() },
                            type = KeyboardType.Password,
                            onValueChange = {
                                senha.value = it
                                userViewModel.password = it
                            })


                        EcoMinimalTextField(cpf,
                            placeholder = "cpf",
                            focusRequester = focusNome,
                            imeAction = ImeAction.Next,
                            onDone = {
                                // Validação do CPF ao sair do campo
                                if (!isValidCPF(cpf.value)) {
                                    cpfError = true
                                    cpfErrorMessage = "CPF inválido"
                                } else {
                                    cpfError = false
                                    cpfErrorMessage = ""
                                    focusDataNasc.requestFocus()
                                }
                            },
                            type = KeyboardType.Number,
                            hasError = cpfError,
                            errorMessage = cpfErrorMessage,
                            visualTransformation = cpfVisualTransformation(),
                            onValueChange = { value ->
                                cpf.value = value
                                userViewModel.cpf = value
                                if (value.isNotEmpty() && !isValidCPF(value)) {
                                    cpfError = true
                                    cpfErrorMessage = "CPF inválido"
                                } else {
                                    cpfError = false
                                    cpfErrorMessage = ""
                                }
                            },)

                        EcoMinimalTextField(
                            dataNasc,
                            placeholder = "data de nascimeno",
                            onValueChange = {
                                dataNasc.value = it
                                userViewModel.dataNasc = it
                            })

                        Spacer(Modifier.size(8.dp))

                    }
                    EcoSimpleButton(onClick = {
                        estado = !estado
                        println(estado)
                        handleSubmit()
                    }, text = "Cadastro")

                }
            }
        }
    }
}