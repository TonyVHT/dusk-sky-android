package com.example.duskskyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint              // 1. importa esto
import com.example.duskskyapp.ui.navigation.AppNavHost  // 2. importa tu NavHost
import com.example.duskskyapp.ui.theme.DuskSkyAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuskSkyAppTheme {
                // 2. reemplaza Greeting/Sacffold por AppNavHost
                AppNavHost()
            }
        }
    }
}
