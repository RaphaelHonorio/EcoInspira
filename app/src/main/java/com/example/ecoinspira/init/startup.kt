package com.example.ecoinspira.init

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Startup: Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@Startup)
            modules(module {

                //--== Injetando serviços de entidade


                viewModel{EcoFragmentsViewModel()

                }

            })
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}