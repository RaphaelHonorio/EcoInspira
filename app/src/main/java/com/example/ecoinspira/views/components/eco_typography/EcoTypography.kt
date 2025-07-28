package com.example.ecoinspira.views.components.eco_typography

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.EcoFontNunitoSans
import com.example.ecoainspira.config.theme.theme

@Composable
fun EcoTypography(
    text : String,
    lineHeight: TextUnit = 20.sp,
    size : TextUnit?= 12.sp,
    weight: FontWeight?= FontWeight.Normal,
    padding: PaddingValues? = PaddingValues(0.dp),
    color : Color = theme.colors.black00,
    textAlign: TextAlign?= TextAlign.Start,
    maxLine: Int ?= 1000
) {
    // --== Iniciando renderização
    Text(text = text, fontSize = size!!, fontWeight = weight,
        textAlign = textAlign, lineHeight = lineHeight, modifier = Modifier.padding(padding!!),
        color = color, fontFamily = EcoFontNunitoSans, maxLines = maxLine!!
    )
}