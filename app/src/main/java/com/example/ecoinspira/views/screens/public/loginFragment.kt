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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


@SuppressLint("UnrememberedMutableState")
@Composable
fun loginFragment() {

    var estado by remember { mutableStateOf(false) }

    var email = mutableStateOf("")
    var senha = mutableStateOf("")
    var nome = mutableStateOf("")
    var emailCad = mutableStateOf("")
    var senhaCad = mutableStateOf("")
    var cpf = mutableStateOf("")
    var dataNasc = mutableStateOf("")


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

                            CustomCheckbox(checked = lembrar, onCheckedChange = { lembrar = it })
                            
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
                        EcoMinimalTextField(nome, placeholder = "nome completp")
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(emailCad, placeholder = "email")
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(senhaCad, placeholder = "senha")
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(cpf, placeholder = "cpf")
                        Spacer(Modifier.size(8.dp))
                        EcoMinimalTextField(dataNasc, placeholder = "data de nascimeno")

                    }
                    EcoSimpleButton(onClick = {
                        estado = !estado
                        println(estado)
                    }, text = "Cadastro")
                }
            }
        }
    }
}



@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    size: Dp = 20.dp,
    checkmarkColor: Color = Color.White,
    boxColor: Color = if (checked) theme.colors.logo else theme.colors.white,
    borderColor: Color =  theme.colors.logo,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .background(boxColor)
            .border(2.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = checkmarkColor,
                modifier = Modifier.size(size * 0.7f)
            )
        }
    }
}