package com.example.ecoainspira.views.components.eco_buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.views.components.eco_typography.EcoTypography

@Composable
fun EcoSimpleButton(
    text: String? = null,
    onClick: (() -> Unit)? = null,
    textSize: TextUnit? = 15.sp,
    fullWidth: Boolean? = null,
    textWeight: FontWeight? = FontWeight.W700,
    backgroundColor: Color? = theme.colors.primary01,
    backgroundColorDegrade: Color? = theme.colors.primary02,
    borderRadius: Dp? = 90.dp,
    height: Dp? = 48.dp,
    widthFloat: Float? = 1f,
    color: Color? = theme.colors.black01,
    loading: Boolean? = false,
    borderColor: Color? = theme.colors.transparent,
    borderSize: Dp? = 10.dp,
) {

    var styledTicketButton = Modifier.wrapContentSize()

    fullWidth?.let { if (fullWidth == true) styledTicketButton = styledTicketButton.fillMaxWidth() }

    Column(
        Modifier
            .clip(RoundedCornerShape(borderRadius!!))
            .border(borderSize!!, borderColor!!, shape = RoundedCornerShape(90.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        backgroundColor!!,
                        backgroundColorDegrade!!
                    )
                )
            )
            .height(height!!)
            .fillMaxWidth(widthFloat!!)
            .clickable(onClick = { onClick?.invoke() })
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        text?.let {

            if (loading == true) {
                CircularProgressIndicator(
                    color = theme.colors.blackTransparent, strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                EcoTypography(
                    text = it, size = textSize, color = color!!,
                    padding = PaddingValues(top = 2.dp), weight = textWeight
                )
            }
        }
    }
}