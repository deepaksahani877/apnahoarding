package com.app.apnahoarding.ui.screens.welcome


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.apnahoarding.R
import com.app.apnahoarding.auth.AuthResponse
import com.app.apnahoarding.auth.AuthenticationManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun WelcomeScreen(
    navHostController: NavHostController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val authenticationManager = AuthenticationManager(context)
    val coroutineScope = rememberCoroutineScope()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.apna_hording),
                contentDescription = "ApnaHoarding Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Welcome to", fontSize = 24.sp, color = Color.Gray)
            Text("ApnaHoarding", fontSize = 32.sp, color = Color.Blue)
            Text(
                "List your walls and connect with advertisers effortlessly.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SocialButton(
                "Login with Google",
                iconRes = R.drawable.google_icon,
                backgroundColor = Color.White,
                textColor = Color.Gray,
                onClick = {
                    authenticationManager.signInWithGoogle()
                        .onEach { response->
                            if(response is AuthResponse.Success){
                                val isComplete = viewModel.isUserProfileComplete()
                                if (isComplete) {
                                    navHostController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                } else {
                                    navHostController.navigate("signup") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            }
                        }
                        .launchIn(coroutineScope)

                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SocialButton(

                "Login with Facebook",
                iconRes = R.drawable.facebook_icon,
                backgroundColor = Color.Blue,
                textColor = Color.White,
                onClick = {
                    navHostController.navigate("profile")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "By continuing you agree with our Terms & Policies",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.clickable() { }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    iconRes: Int,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = textColor)
    }
}