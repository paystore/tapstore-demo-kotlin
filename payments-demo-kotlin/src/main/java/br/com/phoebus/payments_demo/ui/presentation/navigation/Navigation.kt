package br.com.phoebus.payments_demo.ui.presentation.navigation

sealed class Navigation (val route: String) {
    object Splash: Navigation("splash_screen")
    object MainScreen: Navigation("MainScreen")
    object LoginScreen: Navigation("LoginScreen")
    object BiometricConfigScreen: Navigation("BiometricConfigScreen")
}