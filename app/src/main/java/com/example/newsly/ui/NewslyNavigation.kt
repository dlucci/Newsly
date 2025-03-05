package com.example.newsly.ui

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

@Composable
fun NewslyNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            StoryList(navController)
        }
        composable(Routes.Article.route + "/{url}") {
            val url = it.arguments?.getString("url")
            StoryViewer(navController = navController, url = url ?: "")
        }
    }
}

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Article : Routes("article")

}