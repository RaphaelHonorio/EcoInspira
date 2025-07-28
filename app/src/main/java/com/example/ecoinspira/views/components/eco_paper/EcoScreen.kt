package com.example.ecoinspira.views.components.eco_paper

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.config.screen.offsetNavbarVisivel
import com.example.ecoinspira.config.screen.offsetTopBarVisivel
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel

// --== Composição para facilitar padronização de opções de tela
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EcoScreen(
    backgroundColor: Color? = theme.colors.white,
    viewModel: EcoFragmentsViewModel,
    children: (@Composable () -> Unit)?,
) {
    // --== Contexto da composição
    val context = LocalContext.current

    // --== Informações da tela atual
    val telaAtual = viewModel.telaAtual.observeAsState()

    val topPadding by animateDpAsState(
        if (viewModel.topbarView.value?.targetOffSet?.value == offsetTopBarVisivel) {
            70.dp
        } else {
            0.dp
        }, label = "",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
        )
    )

    val bottomPadding by animateDpAsState(
        if (viewModel.navBarView.value?.targetOffSet?.value == offsetNavbarVisivel) {
            (70f).dp
        } else {
            0.dp
        }, label = "",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // --== Iniciando renderização
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor!!)
    ) {

        Column(modifier = Modifier.padding(top = topPadding, bottom = bottomPadding)) {
            children?.invoke()
        }


    }
}

// --== Composição para facilitar padronização de margem
@Composable
fun EcoMargin(
    backgroundColor: Color? = Color.Transparent,
    marginTop: Dp = 8.dp, marginBottom: Dp = 0.dp,
    marginLeft: Dp = 16.dp, marginRight: Dp = 16.dp, children: @Composable (() -> Unit)?,
) {
    // --== Iniciando renderização
    Column(
        modifier = Modifier
            .padding(start = marginLeft, end = marginRight, bottom = marginBottom, top = marginTop)
            .background(backgroundColor!!)
    ) {
        // -- Chamando o filho caso exista
        children?.invoke()
    }
}