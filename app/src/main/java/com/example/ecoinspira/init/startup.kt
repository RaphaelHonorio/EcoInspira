package com.example.ecoinspira.init

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecoinspira.services.generate.EcoGenerateService
import com.example.ecoinspira.services.generate.IEcoGenerateService
import com.example.ecoinspira.services.memo.EcoMemo
import com.example.ecoinspira.services.memo.IEcoMemo
import com.example.ecoinspira.services.post.EcoPostService
import com.example.ecoinspira.services.post.IEcoPostService
import com.example.ecoinspira.services.user.EcoUserService
import com.example.ecoinspira.services.user.IEcoUserService
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.generate.EcoGenerateViewModel
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Startup : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Startup)
            modules(module {

                //--== Injetando serviços de entidade
                single<IEcoGenerateService> { EcoGenerateService() }
                single<IEcoUserService> { EcoUserService() }
                single<IEcoPostService> { EcoPostService() }

                single<IEcoMemo> { EcoMemo() }

                viewModel { EcoUserViewModel(get()) }
                viewModel { EcoGenerateViewModel(get()) }

                viewModel { EcoFragmentsViewModel() }

            })
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}