package com.example.ecoinspira.viewmodel.fragment

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
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ecoinspira.config.screen.EcoFragmentSliderView
import com.example.ecoinspira.config.screen.EcoNavbarSliderView


@Composable
fun EcoFragmentSlider(
    form: State<EcoFragmentSliderView?>,
    children: @Composable () -> Unit
) {
    form.let {
        it.value?.offSet?.value?.let { offSet ->
            it.value?.targetOffSet?.value?.let { targetOffset ->
                EcoPaginationContainer(offSet, targetOffset) {
                    children()
                }
            }
        }
    }
}

@Composable
fun EcoNavbarFragmentSlider(
    form: State<EcoNavbarSliderView?>,
    children: @Composable () -> Unit
) {
    form.let {
        it.value?.offSet?.value?.let { offSet ->
            it.value?.targetOffSet?.value?.let { targetOffset ->
                EcoPaginationContainer(offSet, targetOffset, orientation = TicketPaginationContainerType.Vertical) {
                    children()
                }
            }
        }
    }
}



@Composable
fun EcoPaginationContainer(
    initial: Float, targetOffsetY: Float,
    orientation: TicketPaginationContainerType? = TicketPaginationContainerType.Horizontal,
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
                if (orientation == TicketPaginationContainerType.Horizontal)
                    Modifier.offset(x = offsetY.value.dp) else Modifier.offset(y = offsetY.value.dp)
            )
    ) {
        Column(Modifier.fillMaxWidth().fillMaxHeight()) {
            children()
        }
    }
}

enum class TicketPaginationContainerType {
    Horizontal, Vertical
}