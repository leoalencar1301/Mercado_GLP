package com.mercadoglp.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mercadoglp.app.ui.auth.LoginScreen
import com.mercadoglp.app.ui.auth.RegisterScreen
import com.mercadoglp.app.ui.auth.AuthViewModel
import com.mercadoglp.app.ui.dashboard.DashboardScreen
import com.mercadoglp.app.ui.dashboard.MapScreen
import com.mercadoglp.app.ui.dashboard.StatisticsScreen
import com.mercadoglp.app.ui.premium.PremiumScreen
import com.mercadoglp.app.ui.profile.ProfileScreen
import com.mercadoglp.app.ui.settings.SettingsScreen
import com.mercadoglp.app.ui.splash.SplashScreen

/**
 * Rotas de navegação do aplicativo
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Map : Screen("map/{revendaId}") {
        fun createRoute(revendaId: String) = "map/$revendaId"
    }
    object Statistics : Screen("statistics")
    object Premium : Screen("premium")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

/**
 * Navegação principal do aplicativo
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    
    // Determinar rota inicial baseada no estado de autenticação
    val startDestination = when {
        authState.isLoading -> Screen.Splash.route
        authState.user != null -> Screen.Dashboard.route
        else -> Screen.Login.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Tela de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Tela de Registro
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Dashboard Principal
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToMap = { revendaId ->
                    navController.navigate(Screen.Map.createRoute(revendaId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToPremium = {
                    navController.navigate(Screen.Premium.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Mapa de Revenda Individual
        composable(
            route = Screen.Map.route,
            arguments = listOf(
                navArgument("revendaId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val revendaId = backStackEntry.arguments?.getString("revendaId")
            
            MapScreen(
                revendaId = revendaId ?: "",
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Estatísticas
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Premium/Assinatura
        composable(Screen.Premium.route) {
            PremiumScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSubscriptionSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        // Perfil do Usuário
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // Configurações
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
