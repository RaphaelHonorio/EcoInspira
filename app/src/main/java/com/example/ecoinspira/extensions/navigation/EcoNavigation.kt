package com.example.ecoinspira.extensions.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.ecoinspira.views.screens.public.EcoLoginScreen
import com.example.ecoinspira.views.screens.public.EcoMainScreen
import com.google.gson.Gson

const val defExtra = "EXTID"

enum class EcoScreens {
    Login, Splash, Main
}

fun Context.navigate(dest: EcoScreens, data: Any? = null) {
    val className = if (data == null) "" else data::class.qualifiedName
    val extra: String = if (className != null)
        if (className.startsWith("kotlin.") || className.startsWith("java."))
            Gson().toJson(data) else data.toString() else ""

    // -- Criando Intent com base no destino
    val intent = when (dest) {
        EcoScreens.Login -> Intent(this, EcoLoginScreen::class.java)
        EcoScreens.Splash -> Intent(this, EcoLoginScreen::class.java)
        EcoScreens.Main -> Intent(this, EcoMainScreen::class.java)
    }

    // -- Adicionando extras
    intent.putExtra(defExtra, extra)

    // -- Iniciando tela
    startActivity(intent)

    // --== Finalizando a tela atual
    (this as? Activity)?.finish()
}