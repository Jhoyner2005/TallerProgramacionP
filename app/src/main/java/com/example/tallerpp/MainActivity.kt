package com.example.tallerpp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.tallerpp.ui.theme.TallerPPTheme
import com.example.tallerpp.composables.GameScreen
import com.example.tallerpp.composables.HomeScreen
import com.example.tallerpp.enums.TypeScreen
import com.example.tallerpp.composables.InstructionScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TallerPPTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    var currentScreen by remember {
                        mutableStateOf(TypeScreen.HOME)
                    }

                    var bestScore by remember {
                        mutableStateOf(0)
                    }

                    if (currentScreen == TypeScreen.HOME) {

                        HomeScreen(
                            bestScore = bestScore,
                            onClickInstruction = {
                                currentScreen = TypeScreen.INSTRUCTION
                            },
                            onClickGame = {
                                currentScreen = TypeScreen.GAME
                            }
                        )

                    } else if (currentScreen == TypeScreen.GAME) {

                        GameScreen(
                            onBack = {
                                currentScreen = TypeScreen.HOME
                            },
                            onGameWon = { score ->

                                if (bestScore == 0 || score < bestScore) {
                                    bestScore = score
                                }

                            }
                        )

                    } else if (currentScreen == TypeScreen.INSTRUCTION) {

                        InstructionScreen {
                            currentScreen = TypeScreen.HOME
                        }
                    }
                }
            }
        }
    }
}