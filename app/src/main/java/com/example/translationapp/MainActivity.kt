package com.example.translationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.translationapp.controllers.TranslationAppController
import com.example.translationapp.ui.theme.TranslationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslationAppTheme(dynamicColor = false) {
                TranslationAppController()
            }
        }
    }
}
