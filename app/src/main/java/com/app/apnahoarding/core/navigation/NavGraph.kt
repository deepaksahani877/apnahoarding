package com.app.apnahoarding.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.apnahoarding.ui.screens.addWall.AddWallScreen
import com.app.apnahoarding.ui.screens.editProfile.EditProfileScreen
import com.app.apnahoarding.ui.screens.feed.LatestUpdatesScreen
import com.app.apnahoarding.ui.screens.home.HomeScreen
import com.app.apnahoarding.ui.screens.profile.ProfileScreen
import com.app.apnahoarding.ui.screens.search.BillboardSearchScreen
import com.app.apnahoarding.ui.screens.settings.SettingsScreen
import com.app.apnahoarding.ui.screens.signup.SignUpScreen
import com.app.apnahoarding.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("editProfile") {
            EditProfileScreen(navController)
        }
        composable("addWall") {
            AddWallScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }

        composable("search") {
            BillboardSearchScreen(navController)
        }
        composable("feed") {
            LatestUpdatesScreen(navController)
        }
    }
}
