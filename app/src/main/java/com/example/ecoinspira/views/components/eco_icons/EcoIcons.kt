package com.example.ecoinspira.views.components.eco_icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecoainspira.config.theme.theme
import java.util.UUID

//--== Composição padronizada para consumo de Icone
@Composable
fun EcoIcon(
    icon: ImageVector,
    iconColor: Color? = theme.colors.black01,
    alt: String? = UUID.randomUUID().toString(),
    size: Dp? = 1.dp, onClick: (() -> Unit)? = null,
) {

    if (onClick != null) {

        Column(Modifier.width(size!!).height(size).clickable { onClick?.invoke() }) {
            Icon(
                imageVector = icon, contentDescription = alt, tint = iconColor!!,
                modifier = Modifier.then(if (size != null) Modifier.size(size) else Modifier)
            )
        }
    } else {
        Column(Modifier.width(size!!).height(size)) {
            Icon(
                imageVector = icon, contentDescription = alt, tint = iconColor!!,
                modifier = Modifier.then(if (size != null) Modifier.size(size) else Modifier)
            )
        }
    }
}