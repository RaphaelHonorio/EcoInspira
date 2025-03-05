package com.example.ecoinspira.viewmodel.eco_fragment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.example.ecoinspira.config.screen.EcoFragmentSliderView
import com.example.ecoinspira.config.screen.EcoNavbarSliderView
import com.example.ecoinspira.config.screen.EcoTopbarSliderView
import com.example.ecoinspira.views.components.animation.EcoPaginationContainer
import com.example.ecoinspira.views.components.animation.EcoPaginationContainerType

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
fun EcoTopbarFragmentSlider(
    form: State<EcoTopbarSliderView?>,
    children: @Composable () -> Unit
) {
    form.let {
        it.value?.offSet?.value?.let { offSet ->
            it.value?.targetOffSet?.value?.let { targetOffset ->
               EcoPaginationContainer(offSet, targetOffset, orientation = EcoPaginationContainerType.Vertical) {
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
               EcoPaginationContainer(offSet, targetOffset, orientation = EcoPaginationContainerType.Vertical) {
                    children()
                }
            }
        }
    }
}


