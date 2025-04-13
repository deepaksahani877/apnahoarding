package com.app.apnahoarding.ui.screens.addWall


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.apnahoarding.ui.theme.TopAppBarColor
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Crop
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWallScreen(navController: NavController) {
    val length = remember { mutableStateOf("") }
    val width = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Wall", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopAppBarColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE7F6FC))
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Text(
                "Wall Size :",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF003366)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = length.value,
                    onValueChange = { length.value = it },
                    placeholder = { Text("Length (feet)") },
                    leadingIcon = { Icon(Icons.Default.SquareFoot, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = width.value,
                    onValueChange = { width.value = it },
                    placeholder = { Text("Width (feet)") },
                    leadingIcon = { Icon(Icons.Default.SquareFoot, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Rent per/month :",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF003366)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price.value,
                onValueChange = { price.value = it },
                placeholder = { Text("Enter price") },
                leadingIcon = { Text("₹", fontSize = 20.sp) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Slider(
                value = price.value.toFloatOrNull() ?: 0f,
                onValueChange = { price.value = it.toInt().toString() },
                valueRange = 0f..1000f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Please add exactly 3 images :",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF003366)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF597DBF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Text("Photo ${index + 1}", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Handle continue */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066CC))
            ) {
                Text("CONTINUE", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
