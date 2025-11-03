package com.example.ecoinspira.views.components.eco_buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoainspira.config.theme.theme

@Composable
fun EcoVoltarButton(
    onClick: (() -> Unit)? = null,
) {
    Column {


        EcoSimpleButton(
            "Voltar",
            widthFloat = 0.33f,
            backgroundColor = theme.colors.cinza06,
            backgroundColorDegrade = theme.colors.cinza06,
            borderRadius = 24.dp,
            onClick = onClick,
            height = 40.dp,
            textWeight = FontWeight.W800
        )

        Spacer(modifier = Modifier.width(16.dp))
    }
}