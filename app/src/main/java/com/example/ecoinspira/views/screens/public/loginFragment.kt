package com.example.ecoinspira.views.screens.public

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.services.user.IEcoUserService
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_buttons.EcoCheckbox
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get


@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginFragment(
    userViewModel: EcoUserViewModel
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
                    onSucess = {},
                    onFailure = { error ->
                        println(error)
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


                        EcoMinimalTextField(email, placeholder = "Email")
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(senha, placeholder = "senha")

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
                            estado = !estado
                            println(estado)
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


fun isValidCPF(cpf: String): Boolean {
    val cleanCPF = cpf.replace(Regex("[^\\d]"), "")
    if (cleanCPF.length != 11 || cleanCPF.all { it == cleanCPF[0] }) return false

    fun calcDigit(cpf: String, factor: Int): Int {
        var sum = 0
        var weight = factor
        for (char in cpf) {
            sum += (char.toString().toInt() * weight--)
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    val digit1 = calcDigit(cleanCPF.substring(0, 9), 10)
    val digit2 = calcDigit(cleanCPF.substring(0, 9) + digit1, 11)

    return cleanCPF == cleanCPF.substring(0, 9) + digit1.toString() + digit2.toString()
}

fun cpfVisualTransformation(): VisualTransformation = object : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Garante só dígitos e no máximo 11
        val digits = text.text.filter(Char::isDigit).take(11)

        // Monta "xxx.xxx.xxx-xx" (sem separador pendurado)
        val masked = buildString {
            for (i in digits.indices) {
                append(digits[i])
                when (i) {
                    2 -> if (digits.length > 3) append('.')
                    5 -> if (digits.length > 6) append('.')
                    8 -> if (digits.length > 9) append('-')
                }
            }
        }

        val hasSep1 = digits.length > 3   // ponto após 3º dígito
        val hasSep2 = digits.length > 6   // ponto após 6º dígito
        val hasSep3 = digits.length > 9   // hífen após 9º dígito

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var o = offset
                if (hasSep1 && offset > 3) o += 1
                if (hasSep2 && offset > 6) o += 1
                if (hasSep3 && offset > 9) o += 1
                return o
            }
            override fun transformedToOriginal(offset: Int): Int {
                var t = offset
                if (hasSep1 && t > 3) t -= 1
                if (hasSep2 && t > 7) t -= 1
                if (hasSep3 && t > 11) t -= 1
                return t.coerceIn(0, digits.length)
            }
        }
        return TransformedText(AnnotatedString(masked), mapping)
    }
}