package com.example.ecoinspira.views.screens.public.fragments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


@Composable
fun ConfigFragment (userViewModel: EcoUserViewModel,
                    fragmentMainViewModel: EcoFragmentsViewModel
) {

    EcoFragmentSlider(form = fragmentMainViewModel.configFragmentView.observeAsState()) {

        EcoTypography(text = "Tela de configurações")

    }
}