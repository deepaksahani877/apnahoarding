package com.app.apnahoarding.ui.screens.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF0059C5)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // Use false because the background is dark (blue)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 4.dp,
                actions = {
                    BottomNavigationBar()
                }
            )
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
                    ProfileButton("Profile")
                    ProfileButton("My Posts")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileButton("Dashboard")
                    ProfileButton("Settings")
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
                Text("# deWall Ads", fontWeight = FontWeight.Bold, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProfileButton(text: String) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 60.dp)
            .background(Color(0xFFE9F6FF), shape = RoundedCornerShape(12.dp))
            .clickable { /* TODO */ },
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BottomNavigationBar() {
    val items = listOf("Home", "Search", "Feed", "Profile")
    val icons = listOf(
        Icons.Default.Favorite,
        Icons.Default.Search,
        Icons.Default.GridView,
        Icons.Default.Person
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            if (index == 2) {
                Spacer(modifier = Modifier.width(48.dp)) // Space for FAB
            }

            NavigationBarItem(
                selected = item == "Profile", // You can use a state to manage selection
                onClick = { /* TODO */ },
                icon = {
                    Icon(imageVector = icons[index], contentDescription = item)
                },
                label = {
                    Text(text = item, fontSize = 11.sp)
                },
                alwaysShowLabel = true
            )
        }
    }
}
