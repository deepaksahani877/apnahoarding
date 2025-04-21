package com.app.apnahoarding.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.apnahoarding.R
import com.app.apnahoarding.ui.components.BottomNavFAB
import com.app.apnahoarding.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestUpdatesScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Latest updates", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0066D9)),
            )
        },
        bottomBar = {
            BottomNavigationBar(navHostController,true)
        },

        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                UpdateCard(
                    profileImage = R.drawable.ic_profile,
                    title = "deWall Ads",
                    time = "1 day ago",
                    description = "Normal walls vrs deWall Walls\n\nAB AAP BHI ADS LAGWAO AUR PAISE KAMAO",
                    bottomImage = R.drawable.placeholder_image
                    )

                Spacer(modifier = Modifier.height(12.dp))

                UpdateCard(
                    profileImage = R.drawable.ic_profile,
                    title = "deWall Updates",
                    time = "5 months ago",
                    description = "Turn Your Walls into Earning Assets!\n\nWith deWall Ads, you can monetize your unused",
                    bottomImage = R.drawable.placeholder_image,
                )

                Spacer(modifier = Modifier.height(12.dp))

                UpdateCard(
                    profileImage = R.drawable.ic_profile,
                    title = "deWall Updates",
                    time = "5 months ago",
                    description = "Turn Your Walls into Earning Assets!\n\nWith deWall Ads, you can monetize your unused",
                    bottomImage = R.drawable.placeholder_image,
                )


                Spacer(modifier = Modifier.height(12.dp))

                UpdateCard(
                    profileImage = R.drawable.ic_profile,
                    title = "deWall Updates",
                    time = "5 months ago",
                    description = "Turn Your Walls into Earning Assets!\n\nWith deWall Ads, you can monetize your unused",
                    bottomImage = R.drawable.placeholder_image,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            BottomNavFAB(navHostController)
        }
    )
}



@Composable
fun UpdateCard(
    profileImage: Int,
    title: String,
    time: String,
    description: String,
    showImagePlaceholder: Boolean = false,
    bottomImage: Int? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = profileImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Text(text = time, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description)

            if (showImagePlaceholder) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Placeholder", tint = Color.Gray)
                }
            }

            bottomImage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}