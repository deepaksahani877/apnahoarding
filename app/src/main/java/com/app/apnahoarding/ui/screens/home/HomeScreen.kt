package com.app.apnahoarding.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.apnahoarding.R
import com.app.apnahoarding.core.models.WallData
import com.app.apnahoarding.ui.components.BottomNavFAB
import com.app.apnahoarding.ui.components.BottomNavigationBar
import com.app.apnahoarding.ui.shared.viewmodel.FetchWallUiState
import com.app.apnahoarding.ui.shared.viewmodel.SelectedWallViewModel
import com.app.apnahoarding.ui.shared.viewmodel.WallListViewModel
import com.app.apnahoarding.utils.toFormattedString


@Composable
fun HomeScreen(navController: NavHostController, selectedWallViewModel: SelectedWallViewModel, viewModel: WallListViewModel = hiltViewModel()) {
    var selectedItem by remember { mutableIntStateOf(0) }

    var selectedCity by remember { mutableStateOf("Delhi") }

    val uiStateFeaturedWalls by viewModel.featuredWallsState.collectAsState()
    val uiStateRecentWalls by viewModel.recentWallState.collectAsState()


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadFeaturedWalls()
        viewModel.loadRecentWalls(5)
    }


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, bottomBarState = true)
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5FBFF)) // light blue background
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            // Location & Share Row

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CityDropdownSelector(
                    selectedCity = selectedCity,
                    cityList = listOf("Delhi", "Mumbai", "Bangalore", "Kolkata", "Chennai"),
                    onCitySelected = { selectedCity = it }
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.share_icon),
                    contentDescription = "Share",
                    tint = Color(0xFF007BFF),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search location") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF007BFF),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()) // Enables horizontal scrolling
                    .padding(vertical = 16.dp) // Optional padding
            ) {
                repeat(4) {
                    // Single or multiple banners inside
                    Image(
                        painter = painterResource(id = R.drawable.banner_ad),
                        contentDescription = "Ad Banner",
                        modifier = Modifier
                            .width((LocalConfiguration.current.screenWidthDp - 24).dp) // Or whatever fixed width you want for the scroll
                            .height(160.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp)) // Space between images if you add more
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Explore Cities", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                listOf("Jain Nagar", "Agra", "Suhawali", "Mathura", "Gorakhpur").forEach { city ->
                    Text(
                        text = city,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
//                            .border(1.dp, Color.LightGray, shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Featured Wall", style = MaterialTheme.typography.titleMedium)
                Text(
                    "View All",
                    color = Color(0xFF007BFF),
                    modifier = Modifier.clickable {
                        navController.navigate("search")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            when (uiStateFeaturedWalls) {
                is FetchWallUiState.Loading -> {

//                    Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        repeat(2) {
                            WallCardPlaceholder()
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }

                is FetchWallUiState.Success -> {
                    val walls = (uiStateFeaturedWalls as FetchWallUiState.Success<List<WallData>>).data

//                    Toast.makeText(context, "data loaded", Toast.LENGTH_SHORT).show()


//                    Toast.makeText(context,walls.get(0).addressLine, Toast.LENGTH_SHORT).show()
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        walls.take(5).forEach { wall ->
                            WallCard(
                                location = wall.addressLine,
                                city = wall.city,
                                time = wall.createdAt.toFormattedString(),
                                imageUrl = wall.imageUris[0]!!,
                                onClick = {
                                    selectedWallViewModel.setSelectedWall(wall)
                                    navController.navigate("wallDetails")
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }

                is FetchWallUiState.Error -> {
                    val error = (uiStateFeaturedWalls as FetchWallUiState.Error).message
                    Text(
                        text = "Error loading featured walls",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )

//                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            //How it works Section
            HowItWorksSection()


            //Recently added
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recently Added", style = MaterialTheme.typography.titleMedium)
                Text(
                    "View All",
                    color = Color(0xFF007BFF),
                    modifier = Modifier.clickable {
                        navController.navigate("search")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            when (uiStateRecentWalls) {
                is FetchWallUiState.Loading -> {

//                    Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        repeat(2) {
                            WallCardPlaceholder()
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }

                is FetchWallUiState.Success -> {
                    val walls = (uiStateRecentWalls as FetchWallUiState.Success<List<WallData>>).data

//                    Toast.makeText(context, "data loaded", Toast.LENGTH_SHORT).show()


//                    Toast.makeText(context,walls.get(0).addressLine, Toast.LENGTH_SHORT).show()
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        walls.take(5).forEachIndexed { index, wall ->
                            WallCard(
                                location = wall.addressLine,
                                city = wall.city,
                                time = wall.createdAt.toFormattedString(),
                                imageUrl = wall.imageUris.getOrNull(0) ?: "", // always using first image safely
                                onClick = {
                                    // Navigate or show detail 
                                    selectedWallViewModel.setSelectedWall(wall)
                                    navController.navigate("wallDetails")
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }

                }

                is FetchWallUiState.Error -> {
                    val error = (uiStateRecentWalls as FetchWallUiState.Error).message
                    Text(
                        text = "Error loading featured walls",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )

//                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Benefits to use section
            BenefitsToUseSection()

            Spacer(modifier = Modifier.height(16.dp))

            //Major benefits section
            MajorBenefitsSection()
        }

        BottomNavFAB(navController)
    }
}


@Composable
fun WallCard(location: String, city: String, time: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(location, maxLines = 1, modifier = Modifier.padding(horizontal = 8.dp))
            Text(city, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
            Text(time, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}


@Composable
fun HowItWorksSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD9ECFC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "How it works",
                    color = Color(0xFFFF9900),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Follow these simple steps",
                    color = Color(0xFF6B6B6B),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                val steps = listOf(
                    "Click  (+) icon in app",
                    "Enter size and location detail",
                    "Choose price",
                    "Upload 3 image of your wall",
                    "Submit for approval"
                )

                steps.forEach { step ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF7F00FF), // Purple
                            modifier = Modifier
                                .size(22.dp)
                                .border(1.dp, Color.Black, CircleShape)
                                .padding(2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            color = Color(0xFF333333),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BenefitsToUseSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEDE6FA), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Benefits to use",
                    color = Color(0xFF8000B0),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Easy way to earn money",
                    color = Color(0xFF6B6B6B),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                val benefits = listOf(
                    "Earn from unused wall space",
                    "Monetize empty walls effortlessly",
                    "Support local businesses",
                    "Hassle-free advertisement",
                    "Monthly rent for hosting ads"
                )

                benefits.forEach { benefit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        BenefitTickIcon()
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = benefit,
                            color = Color(0xFF333333),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Simple blue character placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Bottom)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(Color(0xFF4C84FF), radius = size.minDimension / 2)
                    drawCircle(
                        Color.White,
                        center = Offset(size.width * 0.35f, size.height * 0.3f),
                        radius = 10f
                    )
                    drawCircle(
                        Color.White,
                        center = Offset(size.width * 0.55f, size.height * 0.3f),
                        radius = 10f
                    )
                    drawCircle(
                        Color.Black,
                        center = Offset(size.width * 0.35f, size.height * 0.3f),
                        radius = 4f
                    )
                    drawCircle(
                        Color.Black,
                        center = Offset(size.width * 0.55f, size.height * 0.3f),
                        radius = 4f
                    )
                    drawCircle(
                        Color(0xFF6134B8),
                        center = Offset(size.width * 0.65f, size.height * 0.75f),
                        radius = 12f
                    )
                }
            }
        }
    }
}

@Composable
fun BenefitTickIcon() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(Color.White, CircleShape)
            .border(1.5.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF8000B0),
            modifier = Modifier.size(16.dp)
        )
    }
}


@Composable
fun MajorBenefitsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE6F6FB)) // light blue background
            .padding(16.dp)
    ) {
        // Title with divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(
                text = "  MAJOR BENEFITS  ",
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image with overlay text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.DarkGray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon), // replace with your image
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = "Easy | Reliable | Hassle free",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Help section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFD9EEFF)) // light blue card
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Have any questions ?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Drop a message us on our Mail and team will contact you soon..",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* Contact action */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0055CC))
                    ) {
                        Text(text = "CONTACT NOW", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.google_icon), // replace with actual cat image
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}


@Composable
fun CityDropdownSelector(
    selectedCity: String,
    cityList: List<String>,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable { expanded = true }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                tint = Color(0xFF007BFF), // blue
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedCity,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Icon"
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cityList.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun WallCardPlaceholder() {
    Column(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp)
            .padding(8.dp)
            .shimmerPlaceholder(true) // or conditional if you want
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        )
    }
}

fun Modifier.shimmerPlaceholder(visible: Boolean = true): Modifier {
    return if (visible) {
        this
            .background(Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    } else {
        this
    }
}


//@Composable
//@Preview
//fun PreviewHome() {
//    val navController = rememberNavController()
//    HomeScreen(navController = navController)
//}