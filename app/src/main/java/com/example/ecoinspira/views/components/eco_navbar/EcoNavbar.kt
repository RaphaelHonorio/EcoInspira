package com.example.ecoinspira.views.components.eco_navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsNavigation
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoNavbarFragmentSlider
import com.example.ecoinspira.views.components.eco_typography.EcoTypography

@Composable
fun EcoNavbar(
    viewModel: EcoFragmentsViewModel,
    //  clienteViewModel: TicketClienteViewModel? = null,

) {

    // --== Informações da tela atual
    val tela = viewModel.telaAtual.observeAsState()

    // --== Informações de visualização de navbar
    val navbarView = viewModel.navBarView.observeAsState()

    EcoNavbarFragmentSlider(navbarView) {


        Box(Modifier.height(80.dp)) {
            // --== Barra de notificação inferior


            Column(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp), Arrangement.Bottom
            ) {

                // --== Estilo para obter o posicionamento correto
                BottomAppBar(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    theme.colors.white,
                ) {
                    // --== Alinhando conteúdo, centralizando tudo
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(PaddingValues(bottom = 20.dp)),
                        Arrangement.Center, Alignment.CenterVertically,

                        ) {

                        Row(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            Arrangement.SpaceBetween, Alignment.CenterVertically,

                            ) {


                            // --== Opção de Inicio
                            EcoNavbarItem(
                                Icons.Outlined.Home, "Inicio",
                                tela.value?.id == EcoFragmentsNavigation.Feed,

                                ) { viewModel.verFeed() }

                            // --== Opção de amigos
                            EcoNavbarItem(
                                Icons.Default.Group, "Amigos",
                                tela.value?.id == EcoFragmentsNavigation.Analysis,

                                ) { viewModel.verAnalysis() }


                            // --== Opção de configurações
                            EcoNavbarItem(
                                Icons.Outlined.Settings, "Configurações",
                                tela.value?.id == EcoFragmentsNavigation.Config,


                                ) { viewModel.verConfig() }

                            // --== Opção de perfil
                            EcoNavbarItem(
                                Icons.Default.Person, "Perfil",
                                tela.value?.id == EcoFragmentsNavigation.Perfil,
                            ) {
                                viewModel.verPerfil()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EcoNavbarItem(
    icon: ImageVector? = null,
    text: String? = null,
    active: Boolean? = false,
    onClick: (() -> Unit)? = null,

    ) {
    Box {
        Column(
            Modifier
                .width(88.dp)
                .clickable { onClick?.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            EcoTypography(text!!, color = if (active == true) theme.colors.cinza12 else theme.colors.cinza16,
                weight = if (active == true) FontWeight.Bold else FontWeight.Normal)

            icon?.let {
                Icon(
                    imageVector = it, contentDescription = "Teste", modifier = Modifier.size(28.dp),
                    tint = if (active == true) theme.colors.cinza12 else theme.colors.cinza16
                )
            }
        }
    }
}
