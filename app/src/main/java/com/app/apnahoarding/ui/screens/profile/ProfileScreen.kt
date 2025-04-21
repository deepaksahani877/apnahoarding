package com.app.apnahoarding.ui.screens.profile


import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.apnahoarding.ui.components.BottomNavFAB
import com.app.apnahoarding.ui.components.BottomNavigationBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF0059C5)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // Use false because the background is dark (blue)
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, bottomBarState = true)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEAF8FE))
                .padding(innerPadding)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0059C5))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(8.dp),
                        tint = Color(0xFF0059C5)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Prabhat Paswan", color = Color.White, fontSize = 18.sp)
                        Text("prabhatpaswan834@gmail.com", color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(text = "Profile"){
                        navController.navigate("editProfile")
                    }

                    Button("My Posts"){

                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button("Dashboard"){

                    }
                    Button("Settings"){
                        navController.navigate("settings")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Made in India 🇮🇳", fontSize = 12.sp)
                Text("# Apna hoarding Ads", fontWeight = FontWeight.Bold, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }

    BottomNavFAB(navController)
}

@Composable
fun Button(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 60.dp)
            .background(Color(0xFFB2D8F5), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
