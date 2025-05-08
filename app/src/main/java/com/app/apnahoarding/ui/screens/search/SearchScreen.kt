package com.app.apnahoarding.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.apnahoarding.ui.components.BottomNavFAB
import com.app.apnahoarding.ui.components.BottomNavigationBar
import com.app.apnahoarding.ui.shared.viewmodel.SelectedWallViewModel
import com.app.apnahoarding.ui.shared.viewmodel.WallListViewModel


@Composable
fun BillboardSearchScreen(
    navHostController: NavHostController,
    selectedWallViewModel: SelectedWallViewModel,
    viewModel: WallListViewModel = hiltViewModel()
) {
    val walls by viewModel.paginatedWalls.collectAsState()
    val lazyGridState = rememberLazyGridState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.resetAndLoad()
    }

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index == walls.size - 1) {
                    viewModel.loadMoreWalls()
                }
            }
    }

    Scaffold(
        topBar = {
            SearchBarWithFilter(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isNotBlank()) viewModel.searchWalls(it)
                    else viewModel.resetAndLoad()
                }
            )
        },
        bottomBar = { BottomNavigationBar(navHostController, true) },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) { padding ->
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(walls, key = { it.hashCode() }) { wall ->
                BillboardCard(
                    onClick = {
                        selectedWallViewModel.setSelectedWall(wall)
                        navHostController.navigate("wallDetails")
                    },
                    size = "${wall.length}x${wall.width}",
                    location = "${wall.addressLine}, ${wall.city}, ${wall.state}",
                    pincode = wall.pinCode,
                    imageUrl = wall.imageUris.firstOrNull() ?: ""
                )
            }
        }
         BottomNavFAB(navHostController)
    }
}





@Composable
fun SearchBarWithFilter(value: String, onValueChange: (String) -> Unit) {
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
            value = value,
            onValueChange = onValueChange,
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
fun BillboardCard(size: String, location: String, pincode: String, imageUrl: String?,onClick: ()-> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
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

