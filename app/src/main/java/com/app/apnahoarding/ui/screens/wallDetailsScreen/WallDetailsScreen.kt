package com.app.apnahoarding.ui.screens.wallDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.apnahoarding.ui.shared.viewmodel.SelectedWallViewModel
import com.app.apnahoarding.utils.toFormattedString


@Composable
fun WallDetailsScreen(
    navHostController: NavHostController,
    selectedWallViewModel: SelectedWallViewModel
) {
    val selectedWallState by selectedWallViewModel.selectedWall.collectAsState()
    val wall = selectedWallState ?: return  // If no wall is selected, return early

    val imageUrls = wall.imageUris.filterNotNull()
    var selectedImage by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Async image carousel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            AsyncImage(
                model = imageUrls.getOrNull(selectedImage) ?: "",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Back/Menu Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = { /* more menu */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }

            // Carousel controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        selectedImage =
                            if (selectedImage - 1 < 0) imageUrls.lastIndex else selectedImage - 1
                    },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }

                IconButton(
                    onClick = {
                        selectedImage = (selectedImage + 1) % imageUrls.size
                    },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }

            // Dots indicator
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {
                imageUrls.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (index == selectedImage) Color.White else Color.Gray)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status badge
        Text(
            text = if (wall.isAvailableForRent) "Available" else "Not Available",
            color = if (wall.isAvailableForRent) Color.Green else Color.Red,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color(0xFFDFFFD6), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "(${wall.length}x${wall.width}) ${wall.addressLine}-${wall.pinCode}",
            fontSize = 20.sp,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = wall.createdAt.toFormattedString(), color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Table info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
        ) {
            TableRowWithLeftBg("Size", "${wall.length} x ${wall.width} ft")
            Divider(color = Color.LightGray)
            TableRowWithLeftBg("City", wall.city)
            Divider(color = Color.LightGray)
            TableRowWithLeftBg("Price", "₹ ${wall.price}/Month")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Location address", color = Color(0xFF0D47A1), fontSize = 16.sp)
            Text(
                "${wall.addressLine}, ${wall.city}, ${wall.district} - ${wall.pinCode}, ${wall.state}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Open Map */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF4FF))
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF0D47A1))
                Spacer(modifier = Modifier.width(4.dp))
                Text("View On Map", color = Color(0xFF0D47A1))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Uploaded by (placeholder, should bind if available)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Uploaded by", color = Color(0xFF0D47A1), fontSize = 16.sp)
            Text(wall.uploadedBy, color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Enquiry button
        Button(
            onClick = { /* Enquiry */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("ENQUIRY NOW")
        }
    }
}

//
//@Composable
//fun WallDetailsScreen(
//    navHostController: NavHostController,
//    selectedWallViewModel: SelectedWallViewModel
//) {
//    val images = listOf(
//        R.drawable.placeholder_image,  // your drawable resources
//        R.drawable.placeholder_image,
//        R.drawable.placeholder_image
//    )
//    var selectedImage by remember { mutableStateOf(0) }
//    val selectedWallState by selectedWallViewModel.selectedWall.collectAsState()
//
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Image Carousel with Left and Right buttons
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//        ) {
//            Image(
//                painter = painterResource(id = images[selectedImage]),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//
//            // Back and Menu Icons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Top
//            ) {
//                IconButton(onClick = { /* back */ }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                }
//                IconButton(onClick = { /* menu */ }) {
//                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
//                }
//            }
//
//            // Left and Right Buttons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.Center),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                IconButton(
//                    onClick = {
//                        selectedImage =
//                            if (selectedImage - 1 < 0) images.lastIndex else selectedImage - 1
//                    },
//                    modifier = Modifier.background(
//                        Color.Black.copy(alpha = 0.4f),
//                        shape = CircleShape
//                    )
//                ) {
//                    Icon(
//                        Icons.Default.ArrowBack,
//                        contentDescription = "Previous",
//                        tint = Color.White
//                    )
//                }
//
//                IconButton(
//                    onClick = { selectedImage = (selectedImage + 1) % images.size },
//                    modifier = Modifier.background(
//                        Color.Black.copy(alpha = 0.4f),
//                        shape = CircleShape
//                    )
//                ) {
//                    Icon(
//                        Icons.Default.ArrowForward,
//                        contentDescription = "Next",
//                        tint = Color.White
//                    )
//                }
//            }
//
//            // Indicator Dots
//            Row(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                images.forEachIndexed { index, _ ->
//                    Box(
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .size(8.dp)
//                            .clip(CircleShape)
//                            .background(if (index == selectedImage) Color.White else Color.Gray)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Available Badge
//        Text(
//            text = "Available",
//            color = Color.Green,
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .background(Color(0xFFDFFFD6), shape = RoundedCornerShape(12.dp))
//                .padding(horizontal = 12.dp, vertical = 4.dp),
//            fontSize = 12.sp
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Title
//        Text(
//            text = "(12x50) Koiriyapar Dangauli-275306",
//            fontSize = 20.sp,
//            color = Color(0xFF0D47A1),
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        // Today icon and text
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(horizontal = 16.dp)
//        ) {
//            Icon(
//                Icons.Default.CalendarToday,
//                contentDescription = "Today",
//                tint = Color.Gray,
//                modifier = Modifier.size(16.dp)
//            )
//            Spacer(modifier = Modifier.width(4.dp))
//            Text(text = "Today", color = Color.Gray, fontSize = 14.sp)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Table with different left background
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
//        ) {
//            TableRowWithLeftBg(title = "Size", value = "12x50 feet")
//            Divider(color = Color.LightGray, thickness = 1.dp)
//            TableRowWithLeftBg(title = "City", value = "Mau")
//            Divider(color = Color.LightGray, thickness = 1.dp)
//            TableRowWithLeftBg(title = "Price", value = "₹ 700/Month")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Location Address
//        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//            Text(text = "Location address", color = Color(0xFF0D47A1), fontSize = 16.sp)
//            Text(
//                text = "Koiriyapar Dangauli, Mau, Mau (Pincode - 275306) - Uttar Pradesh",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { /* View map */ },
//                shape = RoundedCornerShape(20.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF4FF)),
//                modifier = Modifier.align(Alignment.Start)
//            ) {
//                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF0D47A1))
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(text = "View On Map", color = Color(0xFF0D47A1))
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Uploaded by
//        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//            Text(text = "Uploaded by", color = Color(0xFF0D47A1), fontSize = 16.sp)
//            Text(
//                text = "Satyam Kumar (+917080****13)",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Enquiry Now button
//        Button(
//            onClick = { /* Enquiry */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .height(50.dp),
//            shape = RoundedCornerShape(24.dp)
//        ) {
//            Text(text = "ENQUIRY NOW")
//        }
//    }
//}
//
@Composable
fun TableRowWithLeftBg(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .background(Color(0xFFEFEFEF)) // Light Grey Background
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}