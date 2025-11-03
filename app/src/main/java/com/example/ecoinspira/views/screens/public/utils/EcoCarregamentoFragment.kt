package com.example.ecoinspira.views.screens.public.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography

@Composable
fun EcoCarregamentoFragment(
    viewModel: EcoFragmentsViewModel
) {

    val isVisible = viewModel.isCarregamentoVisible.observeAsState()

    // --== Animação de visibilidade
    val alpha by animateFloatAsState(if (isVisible.value == true) 1f else 0f, label = "")

    EcoFragmentSlider(viewModel.carregamentoFragmentView.observeAsState()) {

        EcoMargin {
            Box(modifier = Modifier.graphicsLayer(alpha = alpha)) {

                if(isVisible.value == true){

                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        // --== Carregamento
                        TicketLoader(title = viewModel.carregamentoFragmentView.value?.title,
                            description = viewModel.carregamentoFragmentView.value?.desc,
                            cancelClick = viewModel.carregamentoFragmentView.value?.onCancel
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun TicketLoader(
    title: String? = null,
    description: String? = null,
    cancel: Boolean? = true,
    cancelClick: (() -> Unit)? = null,
) {
    // --==  para alinhar os elementos
    Column (horizontalAlignment = Alignment.CenterHorizontally){


        Spacer(modifier = Modifier.height(24.dp))
        EcoTypography(text = title ?: "", size = 32.sp, weight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

            // --== LauchedEffect para fazer a animação funcionar
            val animatedProgress = remember { Animatable(0f) }

            LaunchedEffect(Unit) {
                animatedProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1500),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }

            // --== Canvas para fazer colocar a animação
            Canvas(modifier = Modifier.size(120.dp)) {
                drawArc(
                    color = theme.colors.cinza04,
                    startAngle = (animatedProgress.value * 360) - 88f,
                    sweepAngle = 360f, useCenter = false, style = Stroke(32f)
                )
                drawArc(
                    color = theme.colors.primary01,
                    startAngle = (animatedProgress.value * 360) - 88f,
                    sweepAngle = animatedProgress.value * 360,
                    useCenter = false,
                    style = Stroke(width = 32f, cap = StrokeCap.Round)
                )
            }
        }
        // --== Espaçamento e Descrição
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            EcoTypography(text = description ?: "",
                size = 22.sp,weight = FontWeight.SemiBold,
                textAlign = TextAlign.Center)
        }

        // --== Botão de Cancelar
        Spacer(modifier = Modifier.height(24.dp))

        if (cancel == true){

            EcoSimpleButton(height = 48.dp, fullWidth = true, backgroundColor = theme.colors.paper, backgroundColorDegrade = theme.colors.paper,
                borderColor = theme.colors.black01, borderSize = 1.dp, text = "Cancelar", onClick = cancelClick)

        }
    }
}

@Composable
fun TicketBaseLoader() {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
        Canvas(modifier = Modifier.size(120.dp)) {
            drawArc(
                color = theme.colors.cinza04,
                startAngle = (animatedProgress.value * 360) - 88f,
                sweepAngle = 360f, useCenter = false, style = Stroke(32f)
            )
            drawArc(
                color = theme.colors.primary01,
                startAngle = (animatedProgress.value * 360) - 88f,
                sweepAngle = animatedProgress.value * 360,
                useCenter = false,
                style = Stroke(width = 32f, cap = StrokeCap.Round)
            )
        }
    }
}