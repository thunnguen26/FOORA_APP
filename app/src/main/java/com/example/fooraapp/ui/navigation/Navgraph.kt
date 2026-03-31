package com.example.fooraapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fooraapp.ui.screens.auth.LoginScreen
import com.example.fooraapp.ui.screens.auth.RegisterScreen
import com.example.fooraapp.ui.screens.home.HomeScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login_screen"
    ) {
        // Màn hình đăng nhập
        composable("login_screen") {
            LoginScreen(navController = navController)
        }

        // Màn hình đăng ký
        composable("register_screen") {
            RegisterScreen(navController = navController)
        }

        // Màn hình trang chủ (Home)
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
    }
}