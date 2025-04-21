package com.app.apnahoarding.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.apnahoarding.ui.components.BottomNavigationBar
import com.app.apnahoarding.R
import com.app.apnahoarding.ui.components.BottomNavFAB
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BillboardSearchScreen(navHostController: NavHostController) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF0059C5)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // Use false because the background is dark (blue)
        )
    }

    Scaffold(
        topBar = { SearchBarWithFilter() },
        bottomBar = {
            BottomNavigationBar(navHostController, true)
        },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) { padding ->
        LazyVerticalGrid( // Make this the scrolling container
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(12.dp) // Apply padding here
        ) {
            items(10) { index ->
                BillboardCard(
                    size = if (index == 5) "5.8x8.5" else "10x10",
                    location = "Amwa Bhari, Bahraich, State - Uttarparidesh",
                    pincode = "271882",
                    imageRes = if (index == 5) R.drawable.placeholder_image else null
                )
            }
        }

//        Spacer(modifier = Modifier.height(100.dp)) // Give space for bottom nav

        BottomNavFAB(navHostController)


    }
}


@Composable
fun SearchBarWithFilter() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF0066CC))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
        }
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search pincode/city/size", color = Color.White) },
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFF0066CC), shape = RoundedCornerShape(50)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0066CC),
                unfocusedContainerColor = Color(0xFF0066CC),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }
        )
    }
}


@Composable
fun BillboardCard(size: String, location: String, pincode: String, imageRes: Int?) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFCBDFF7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.BrokenImage,
                        contentDescription = "No Image",
                        tint = Color.Gray
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text("($size) $location", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Pincode : $pincode", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

