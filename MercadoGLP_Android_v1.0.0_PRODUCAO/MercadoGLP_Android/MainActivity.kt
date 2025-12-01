package com.mercadoglp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mercadoglp.app.ui.navigation.AppNavigation
import com.mercadoglp.app.ui.theme.MercadoGLPTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal do aplicativo Mercado do GLP
 * 
 * Features:
 * - Autenticação de usuários
 * - Análise de dados ANP
 * - Visualização em mapas
 * - Sistema premium
 * - Multi-idioma
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash Screen
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        setContent {
            MercadoGLPTheme {
                MercadoGLPApp()
            }
        }
    }
}

@Composable
fun MercadoGLPApp() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    
    // Configurar cor da barra de status
    val useDarkIcons = !isSystemInDarkTheme()
    
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = androidx.compose.ui.graphics.Color.Transparent,
            darkIcons = useDarkIcons
        )
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavigation(navController = navController)
    }
}

@Composable
fun isSystemInDarkTheme(): Boolean {
    return androidx.compose.foundation.isSystemInDarkTheme()
}
