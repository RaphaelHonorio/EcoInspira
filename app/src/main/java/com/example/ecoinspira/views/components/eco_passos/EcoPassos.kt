package com.example.ecoinspira.views.components.eco_passos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.models.generate.EcoGenerateStepsModel
import com.example.ecoinspira.models.generate.Passo
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_icons.EcoIcon
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


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
fun PassoItem(passo: Passo) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(theme.colors.cinza06, RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Cabeçalho (apenas número do passo)
            EcoTypography(
                text = "Passo ${passo.ordem}", size = 16.sp
                )

            if (expanded){
                EcoIcon(icon = Icons.Default.Close, size = 24.dp)
            }
            else{
                EcoIcon(icon = Icons.Default.ArrowDropDown, size = 24.dp)
            }
        }
        // Quando clicado, mostra o texto
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            EcoTypography(
                text = passo.descricao, size = 16.sp
                )
        }
    }
}