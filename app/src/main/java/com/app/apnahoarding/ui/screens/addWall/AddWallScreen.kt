package com.app.apnahoarding.ui.screens.addWall

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.apnahoarding.core.models.WallData
import com.app.apnahoarding.ui.shared.viewmodel.SharedAddWallViewModel
import com.app.apnahoarding.ui.theme.TopAppBarColor
import com.app.apnahoarding.utils.launchCamera

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWallScreen(
    navController: NavHostController,
    sharedAddWallViewModel: SharedAddWallViewModel,
    viewModel: AddWallViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    val onSubmit = {
        val isValid = viewModel.validateForm()

        sharedAddWallViewModel.updateWallData(
            WallData(
                length = state.length,
                width = state.width,
                price = state.price,
                imageUris = state.imageUris.map { it.toString() })
        )

        if (isValid) {
            // Proceed with form submission (e.g., save data)
            navController.navigate("imageUpload")

        } else {
            Toast.makeText(context,"Please fill all the details", Toast.LENGTH_SHORT).show()
            // Handle invalid case (show errors, trigger animations)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Wall", color = Color.White) }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = TopAppBarColor)
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WallSizeInput(
                length = state.length,
                width = state.width,
                onLengthChange = viewModel::onLengthChange,
                onWidthChange = viewModel::onWidthChange
            )

            PriceInput(
                price = state.price, onPriceChange = viewModel::onPriceChange
            )

            ImagePickerRow(
                imageUris = state.imageUris, onImageSelected = { index, uri ->
                    sharedAddWallViewModel.updateImageUri(index, uri)
                    viewModel.updateImageUri(index, uri)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066CC))
            ) {
                Text("CONTINUE", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun WallSizeInput(
    length: String, width: String, onLengthChange: (String) -> Unit, onWidthChange: (String) -> Unit
) {
    Text("Wall Size:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF003366))

    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = length,
            onValueChange = onLengthChange,
            placeholder = { Text("Length (feet)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = width,
            onValueChange = onWidthChange,
            placeholder = { Text("Width (feet)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
fun PriceInput(price: String, onPriceChange: (String) -> Unit) {
    Text(
        "Rent per/month:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF003366)
    )
    OutlinedTextField(
        value = price,
        onValueChange = onPriceChange,
        placeholder = { Text("Enter price") },
        leadingIcon = { Text("₹", fontSize = 20.sp) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Slider(
        value = price.toFloatOrNull() ?: 0f,
        onValueChange = { onPriceChange(it.toInt().toString()) },
        valueRange = 0f..1000f,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun ImagePickerRow(
    imageUris: List<Uri?>, onImageSelected: (index: Int, uri: Uri?) -> Unit
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableIntStateOf(0) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) onImageSelected(selectedIndex.intValue, cameraImageUri.value)

    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> onImageSelected(selectedIndex.intValue, uri) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera(context, cameraImageUri, cameraLauncher)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        repeat(3) { index ->
            ImageSelector(index, imageUris[index]) {
                selectedIndex.intValue = index
                showDialog.value = true
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Select Image Source") },
            text = {
                Column {
                    TextButton(onClick = {
                        galleryLauncher.launch("image/*")
                        showDialog.value = false
                    }) {
                        Text("Gallery")
                    }
                    TextButton(onClick = {
                        showDialog.value = false
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            launchCamera(context, cameraImageUri, cameraLauncher)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Text("Camera")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {})
    }
}


@Composable
fun ImageSelector(index: Int, imageUri: Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF597DBF))
            .clickable { onClick() }
            ,
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(model = imageUri, contentDescription = null)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("Photo ${index + 1}", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}