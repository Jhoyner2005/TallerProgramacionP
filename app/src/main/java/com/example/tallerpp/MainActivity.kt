package com.example.tallerpp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tallerpp.ui.theme.TallerPPTheme
import com.example.tallerpp.composables.GameScreen
import com.example.tallerpp.composables.HomeScreen
import com.example.tallerpp.enums.TypeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TallerPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by remember { mutableStateOf(TypeScreen.HOME) }

                    if (currentScreen == TypeScreen.HOME) {
                        HomeScreen() {
                            currentScreen = TypeScreen.GAME
                        }
                    } else if (currentScreen == TypeScreen.GAME) {
                        GameScreen() {
                            currentScreen = TypeScreen.HOME
                        }
                    }
                }
            }
        }
    }
}