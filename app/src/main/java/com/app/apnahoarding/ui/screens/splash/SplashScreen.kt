package com.app.apnahoarding.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.apnahoarding.R

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is SplashUiState.NavigateHome -> {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            is SplashUiState.NavigateSignUp -> {

                Log.d("messageTest","Signup block")
                navController.navigate("signup") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            is SplashUiState.InvalidUser ->{
                navController.navigate("welcome") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            is SplashUiState.Error -> {
                navController.navigate("welcome") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            SplashUiState.Loading -> {
                // do nothing, just show the progress indicator
            }
        }
    }

    // Trigger the check once when composable is first launched
    LaunchedEffect(Unit) {
        viewModel.checkUserProfile()
    }


    // Splash screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.apna_hording),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = Color.Blue,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
