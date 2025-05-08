package com.app.apnahoarding.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.apnahoarding.ui.screens.addWall.AddWallScreen
import com.app.apnahoarding.ui.screens.editProfile.EditProfileScreen
import com.app.apnahoarding.ui.screens.feed.LatestUpdatesScreen
import com.app.apnahoarding.ui.screens.home.HomeScreen
import com.app.apnahoarding.ui.screens.imageUpload.UploadImageScreen
import com.app.apnahoarding.ui.screens.profile.ProfileScreen
import com.app.apnahoarding.ui.screens.search.BillboardSearchScreen
import com.app.apnahoarding.ui.screens.settings.SettingsScreen
import com.app.apnahoarding.ui.screens.signup.SignUpScreen
import com.app.apnahoarding.ui.screens.splash.SplashScreen
import com.app.apnahoarding.ui.screens.wallDetailsScreen.WallDetailsScreen
import com.app.apnahoarding.ui.screens.welcome.WelcomeScreen
import com.app.apnahoarding.ui.shared.viewmodel.SelectedWallViewModel
import com.app.apnahoarding.ui.shared.viewmodel.SharedAddWallViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = "splash") {
//        composable("home") {
//            HomeScreen(navController)
//        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("editProfile") {
            EditProfileScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable("profile") {
            ProfileScreen(navController)
        }


        composable("feed") {
            LatestUpdatesScreen(navController)
        }

        composable("splash") {
            SplashScreen(navController)
        }


        navigation(route = "wallDetailsFlow", startDestination = "home") {
            composable("home") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("wallDetailsFlow")
                }
                val sharedViewModel = hiltViewModel<SelectedWallViewModel>(parentEntry)
                HomeScreen(navController, sharedViewModel)
            }

            composable("search") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("wallDetailsFlow")
                }
                val sharedViewModel = hiltViewModel<SelectedWallViewModel>(parentEntry)
                BillboardSearchScreen(navController, sharedViewModel)
            }

            composable("wallDetails") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("wallDetailsFlow")
                }
                val sharedViewModel = hiltViewModel<SelectedWallViewModel>(parentEntry)
                WallDetailsScreen(navController, sharedViewModel)
            }
        }


        navigation(
            route = "addWallFlow",
            startDestination = "addWall"
        ) {
            composable("addWall") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("addWallFlow")
                }
                val sharedViewModel = hiltViewModel<SharedAddWallViewModel>(parentEntry)
                AddWallScreen(navController, sharedViewModel)
            }

            composable("imageUpload") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("addWallFlow")
                }
                val sharedViewModel = hiltViewModel<SharedAddWallViewModel>(parentEntry)
                UploadImageScreen(navController, sharedViewModel)
            }
        }
    }
}
