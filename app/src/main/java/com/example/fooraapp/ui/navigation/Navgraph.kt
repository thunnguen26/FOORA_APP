package com.example.fooraapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fooraapp.ui.screens.auth.LoginScreen
import com.example.fooraapp.ui.screens.auth.RegisterScreen
import com.example.fooraapp.ui.screens.detail.DetailScreen
import com.example.fooraapp.ui.screens.home.HomeScreen
import com.example.fooraapp.ui.screens.profile.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupNavGraph(navController: NavHostController) {
    // 1. Kiểm tra xem đã có user đăng nhập trong bộ nhớ máy chưa
    val currentUser = FirebaseAuth.getInstance().currentUser

    // 2. Nếu có rồi thì cho vào thẳng Home, chưa có thì ra màn hình Login
    val startScreen = if (currentUser != null) "home_screen" else "login_screen"

    NavHost(
        navController = navController,
        startDestination = startScreen // ⬅️ Đổi từ "login_screen" thành biến startScreen
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

        // Trong NavHost của NavGraph.kt
        composable(
            route = "detail_screen/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            DetailScreen(navController = navController, productId = productId)

        }
        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }

    }
}