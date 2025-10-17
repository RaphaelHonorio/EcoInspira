package com.example.ecoinspira.views.screens.public

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.example.ecoinspira.services.memo.IEcoMemo
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.generate.EcoGenerateViewModel
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_paper.EcoScreen
import com.example.ecoinspira.views.screens.public.fragments.ConfigFragment
import com.example.ecoinspira.views.screens.public.fragments.FeedFragment
import com.example.ecoinspira.views.screens.public.fragments.PerfilFragment
import com.example.ecoinspira.views.screens.public.fragments.postagem.PostagemFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EcoMainScreen : EcoActivity() {

    private val _fragmentMainViewModel: EcoFragmentsViewModel by viewModel()
    private val _fragmentUserViewModel: EcoUserViewModel by viewModel()

    private val _generateViewModel: EcoGenerateViewModel by viewModel()

    private val _memoService: IEcoMemo by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            LaunchedEffect(Unit){_fragmentMainViewModel.abrirNavbar(); _fragmentMainViewModel.verConfig()}


            EcoScreen(viewModel = _fragmentMainViewModel) {



                Box{

                    FeedFragment(_fragmentMainViewModel)

                    PerfilFragment(_fragmentUserViewModel, _fragmentMainViewModel)

                    ConfigFragment(_fragmentUserViewModel, _fragmentMainViewModel, _memoService)

                    PostagemFragment(_fragmentMainViewModel, _generateViewModel)
                }
            }
        }
    }
}