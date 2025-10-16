package com.example.ecoinspira.views.screens.public.fragments

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


@SuppressLint("UnrememberedMutableState")
@Composable
fun PerfilFragment(
    userViewModel: EcoUserViewModel,
    fragmentMainViewModel: EcoFragmentsViewModel
) {

    EcoFragmentSlider(form = fragmentMainViewModel.perfilFragmentView.observeAsState()) {

       EcoTypography(text = "Tela de Perfil")

    }
}
