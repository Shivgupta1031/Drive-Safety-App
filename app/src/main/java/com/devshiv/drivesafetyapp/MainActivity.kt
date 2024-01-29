package com.devshiv.drivesafetyapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devshiv.drivesafetyapp.screens.EditProfileScreen
import com.devshiv.drivesafetyapp.screens.HomeScreen
import com.devshiv.drivesafetyapp.screens.LoginScreen
import com.devshiv.drivesafetyapp.screens.ReportScreen
import com.devshiv.drivesafetyapp.screens.SplashScreen
import com.devshiv.drivesafetyapp.ui.theme.PrimaryDarkColor
import com.devshiv.drivesafetyapp.ui.theme.SoulVerseAppTheme
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoulVerseAppTheme {
                AppUI()
            }
        }
    }
}

@Composable
fun AppUI() {
    val navController = rememberNavController()
    val context = LocalContext.current as MainActivity

    BackHandler(enabled = true) {
        context.finish()
    }

    NavHost(
        navController = navController, startDestination = Constants.SPLASH_NAV,
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        modifier = Modifier.background(PrimaryDarkColor)
    ) {
        composable(route = Constants.SPLASH_NAV) {
            SplashScreen {
                navController.navigate(it)
            }
        }

        composable(route = "${Constants.LOGIN_NAV}/{logout}") { navBackStack ->
            val logout = navBackStack.arguments?.getString("logout")?.toBoolean() ?: false
            LoginScreen(logout ?: false) {
                navController.navigate(it)
            }
        }

        composable(route = Constants.HOME_NAV) {
            HomeScreen(onNavigateRequired = {
                navController.navigate(it)
            }, onLogout = {
                navController.navigate("${Constants.LOGIN_NAV}/true")
            })
        }

        composable(route = "${Constants.EDIT_PROFILE_NAV}/{prevPage}/{number}") { navBackStack ->
            val prevPage = navBackStack.arguments?.getString("prevPage")
            val number = navBackStack.arguments?.getString("number")
            EditProfileScreen(prevPage ?: Constants.HOME_NAV, number!!) {
                navController.navigate(it)
            }
        }

        composable(route = "${Constants.REPORT_ACCIDENT_NAV}/{prevPage}/{number}") { navBackStack ->
            val prevPage = navBackStack.arguments?.getString("prevPage")
            val number = navBackStack.arguments?.getString("number")
            ReportScreen(prevPage ?: Constants.HOME_NAV, number!!) {
                navController.navigate(it)
            }
        }

    }
}