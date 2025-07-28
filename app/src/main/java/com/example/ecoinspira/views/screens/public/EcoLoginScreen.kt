package com.example.ecoinspira.views.screens.public

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.views.components.eco_paper.EcoScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class EcoLoginScreen : EcoActivity() {

    private val _fragmentMainViewModel: EcoFragmentsViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            EcoScreen(viewModel = _fragmentMainViewModel) {

                loginFragment()

            }
        }
    }
}