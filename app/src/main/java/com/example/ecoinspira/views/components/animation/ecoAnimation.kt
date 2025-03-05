package com.example.ecoinspira.views.components.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun EcoPaginationContainer(
    initial: Float, targetOffsetY: Float,
    orientation: EcoPaginationContainerType? = EcoPaginationContainerType.Horizontal,
    children: @Composable () -> Unit,
) {

    val offsetY = remember { Animatable(initial) }
    LaunchedEffect(targetOffsetY) {
        offsetY.animateTo(
            targetValue = targetOffsetY,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().zIndex(1f)
            .then(
                if (orientation == EcoPaginationContainerType.Horizontal)
                    Modifier.offset(x = offsetY.value.dp) else Modifier.offset(y = offsetY.value.dp)
            )
    ) {
        Column(Modifier.fillMaxWidth().fillMaxHeight()) {
            children()
        }
    }
}

enum class EcoPaginationContainerType {
    Horizontal, Vertical
}
