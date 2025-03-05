package com.example.ecoinspira.views.components.eco_navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.TableRestaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.eco_fragment.EcoNavbarFragmentSlider
import com.example.ecoinspira.views.components.eco_icons.EcoIcon

@Composable
fun EcoNavbar(
    viewModel: EcoFragmentsViewModel,
    ) {
    // --== Informações da tela atual
    val tela = viewModel.telaAtual.observeAsState()

    // --== Informações de visualização de navbar
    val navbarView = viewModel.navBarView.observeAsState()



    EcoNavbarFragmentSlider(navbarView) {

        Box(
            modifier = Modifier
                .height(120.dp)
                .background(theme.colors.transparent)
        ) {

                Box(
                    modifier =
                    Modifier
                        .offset(y = -12.dp, x = 154.dp)
                        .height(52.dp)
                        .width(52.dp)
                        .background(
                            theme.colors.primary01,
                            shape = RoundedCornerShape(90.dp)
                        )
                        .shadow(1.dp, shape = RoundedCornerShape(90.dp))
                        .zIndex(1000f)

                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                       EcoIcon(
                            icon = Icons.Default.RestaurantMenu,
                            size = 28.dp,
                            onClick = { viewModel.verLogin() })
                    }
                }

        }
    }
}