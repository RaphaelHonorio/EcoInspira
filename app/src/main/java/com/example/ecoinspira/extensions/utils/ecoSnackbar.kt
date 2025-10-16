package com.example.ecoinspira.extensions.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import com.google.android.material.snackbar.Snackbar

fun ecoSnackbar(context: Context, message: String, duration: Int = Snackbar.LENGTH_SHORT, color: Int = Color.RED) {
    val snackbar = Snackbar.make((context as Activity).findViewById(android.R.id.content), message, duration)

    // Altera a cor de fundo da Snackbar
    snackbar.view.setBackgroundColor(color)

    snackbar.show()
}