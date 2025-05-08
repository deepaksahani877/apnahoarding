package com.app.apnahoarding.ui.screens.imageUpload

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.apnahoarding.ui.shared.viewmodel.SharedAddWallViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadImageScreen(
    navHostController: NavHostController,
    sharedAddWallViewModel: SharedAddWallViewModel,
    viewModel: UploadImageViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState

    val uploadProgressState by sharedAddWallViewModel.uploadProgress.collectAsState()
    val uploadState by sharedAddWallViewModel.isUploading.collectAsState()
    val context = LocalContext.current




    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setLoading(true)
            viewModel.requestLocationIfPermitted(
                context = context,
                onSuccess = { (lat, lon) ->
                    viewModel.fetchAddressFromCoordinates(context, lat, lon)
                    viewModel.setLoading(false)
                },
                onError = {
                    Toast.makeText(
                        context,
                        "Failed to get location: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.setLoading(false)
                },
                onDenied = {
                    Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
                    viewModel.setLoading(false)
                }
            )
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Upload Image") }) },
        bottomBar = {
            Button(
                onClick = {

                    if (!viewModel.validateAddressForm()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }


                    sharedAddWallViewModel.updateAddressFields(
                        addressLine = uiState.addressLine,
                        city = uiState.city,
                        district = uiState.district,
                        pinCode = uiState.pinCode,
                        state = uiState.state,
                        isAvailableForRent = uiState.isAvailableForRent
                    )


                    sharedAddWallViewModel.updateAddressFields(
                        addressLine = viewModel.uiState.value.addressLine,
                        city = viewModel.uiState.value.city,
                        district = viewModel.uiState.value.district,
                        pinCode = viewModel.uiState.value.pinCode,
                        state = viewModel.uiState.value.state,
                        isAvailableForRent = viewModel.uiState.value.isAvailableForRent
                    )

                    sharedAddWallViewModel.uploadImagesAndSave(
                        localImageUris = sharedAddWallViewModel.uiState.value.imageUris,
                        onSuccess = {
                            Toast.makeText(context, "Wall submitted!", Toast.LENGTH_SHORT).show()
                            navHostController.navigate("home") {
                                popUpTo("addWallFlow") {
                                    inclusive = true
                                }
                            }
                        },
                        onFailure = {
                            Toast.makeText(
                                context,
                                "Error: ${it.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("SUBMIT FOR APPROVAL")
            }
        }
    ) { padding ->


        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Address Fields
            OutlinedTextField(
                value = uiState.addressLine,
                onValueChange = {
                    viewModel.onAddressLineChanged(it)
                    Toast.makeText(
                        context,
                        sharedAddWallViewModel.uiState.value.imageUris[0].toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        context,
                        sharedAddWallViewModel.uiState.value.imageUris[1].toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        context,
                        sharedAddWallViewModel.uiState.value.imageUris[2].toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }, // You can optionally allow editing
                label = { Text("Address Line") },
                isError = uiState.addressLineError,
                supportingText = {
                    if (uiState.addressLineError) Text("Required", color = Color.Red)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shake(uiState.addressLineError)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.city,
                onValueChange = { viewModel.onCityChanged(it) },
                label = { Text("City") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))


            Row {
                OutlinedTextField(
                    value = uiState.district,
                    onValueChange = { viewModel.onDistrictChanged(it) },
                    label = { Text("District") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = uiState.pinCode,
                    onValueChange = { viewModel.onPinCodeChanged(it) },
                    label = { Text("Pincode") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

//            OutlinedTextField(
//                value = uiState.state,
//                onValueChange = { viewModel.onStateChanged(it) },
//                label = { Text("State") },
//                modifier = Modifier.fillMaxWidth()
//            )

            StateDropdown(uiState,viewModel)

            Spacer(Modifier.height(16.dp))

            if (uiState.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            TextButton(
                onClick = {
                    viewModel.setLoading(true)
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Green)
                Spacer(Modifier.width(4.dp))
                Text("Use my current location", color = Color.Green)
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Visibility", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text("Site is available for rent")
                Switch(
                    checked = uiState.isAvailableForRent,
                    onCheckedChange = { viewModel.updateAvailability(it) }
                )
            }
            UploadProgressDialog(uploadState, uploadProgressState)

        }
    }
}


@Composable
fun UploadProgressDialog(
    isVisible: Boolean,
    progress: Float
) {
    if (isVisible) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .width(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Uploading...", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${(progress * 100).toInt()}%")
                }
            }
        }
    }
}


fun Modifier.shake(enabled: Boolean): Modifier = composed {
    var shouldShake by remember { mutableStateOf(false) }
    val offsetX = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(enabled) {
        if (enabled) {
            shouldShake = true
            scope.launch {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 100)
                )

                val delta = listOf(-10f, 10f, -8f, 8f, -5f, 5f, 0f)
                for (x in delta) {
                    offsetX.snapTo(x)
                    kotlinx.coroutines.delay(30)
                }

                shouldShake = false
            }
        }
    }

    this.then(
        Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdown(uiState: UploadImageUiState, viewModel: UploadImageViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val indianStates = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
        "Uttar Pradesh", "Uttarakhand", "West Bengal",
        "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu",
        "Delhi", "Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = uiState.state,
            onValueChange = {},
            label = { Text("State") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            indianStates.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state) },
                    onClick = {
                        viewModel.onStateChanged(state)
                        expanded = false
                    }
                )
            }
        }
    }
}

