package com.example.ecoinspira.views.screens.public

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.example.ecoinspira.views.components.eco_typography.EcoTypography

class EcoLoginScreen : EcoActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            Row {
                EcoTypography("aaaa")
            }

        }
    }
}