package com.example.kalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

object AppRoutes {
    const val MAIN_MENU = "MainActivity"
    const val CALCULATOR = "Kalkulator"
    const val TEXT_EDITOR = "Texteditor"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainMenuApp()
        }
    }
}


@Composable
fun MainMenuApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = AppRoutes.MAIN_MENU) {
                composable(AppRoutes.MAIN_MENU) {
                    MainMenuScreen(
                        onCalculatorClick = { navController.navigate(AppRoutes.CALCULATOR) },
                        onEditorClick = { navController.navigate(AppRoutes.TEXT_EDITOR) }
                    )
                }
                composable(AppRoutes.CALCULATOR) {
                    Kalkulator(navController = navController)
                }
                composable(AppRoutes.TEXT_EDITOR) {
                    TextEditor(navController = navController)
                }
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    onCalculatorClick: () -> Unit,
    onEditorClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onCalculatorClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Kalkulator")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onEditorClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Text Editor")
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewMainMenu() {
    MainMenuApp()
}
