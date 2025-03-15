package com.example.newsly.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun NewslyNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            StoryList(navController)
        }
        composable(Routes.Article.route + "/{url}") {
            val url = it.arguments?.getString("url")
            AnimatedContent(
                targetState = it.arguments?.getString("url"),
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                }
            ) {
                StoryViewer(navController = navController, url = url ?: "")
            }

        }
    }
}

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Article : Routes("article")
}