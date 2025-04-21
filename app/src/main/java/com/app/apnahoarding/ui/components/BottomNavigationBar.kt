package com.app.apnahoarding.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.*
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

import com.app.apnahoarding.R
import com.app.apnahoarding.ui.components.CustomColors.blue_2F52E0
import com.app.apnahoarding.ui.components.CustomColors.grey_98999a


object CustomColors {
    val white_ffffff = Color(0xFFFFFFFF)
    val grey_f4f4f4 = Color(0xFFF4F4F4)
    val grey_98999a = Color(0xFF98999A)
    val blue_2F52E0 = Color(0xFF2F52E0)
}

@Composable
fun CustomText(
    text: String,
    fontColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = fontColor,
        fontSize = 12.sp,
        style = TextStyle(fontWeight = FontWeight.Medium),
        modifier = modifier
    )
}


@Composable
fun CustomIcon(
    id: Int,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = modifier,
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}


class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2
        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 2.5f
            val cutoutLeftX = cutoutCenterX - cutoutEdgeOffset
            val cutoutRightX = cutoutCenterX + cutoutEdgeOffset

            // bottom left
            moveTo(x = 0F, y = size.height.toFloat())
            // top left
            if (cutoutLeftX > 0) {
                val realLeftCornerDiameter = if (cutoutLeftX >= cornerRadiusPx) {
                    // there is a space between rounded corner and cutout
                    cornerDiameter
                } else {
                    // rounded corner and cutout overlap
                    cutoutLeftX * 2
                }
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = realLeftCornerDiameter,
                        bottom = realLeftCornerDiameter
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            lineTo(cutoutLeftX, 0f)
            // cutout
            cubicTo(
                x1 = cutoutCenterX - cutoutRadius,
                y1 = 0f,
                x2 = cutoutCenterX - cutoutRadius,
                y2 = cutoutRadius,
                x3 = cutoutCenterX,
                y3 = cutoutRadius,
            )
            cubicTo(
                x1 = cutoutCenterX + cutoutRadius,
                y1 = cutoutRadius,
                x2 = cutoutCenterX + cutoutRadius,
                y2 = 0f,
                x3 = cutoutRightX,
                y3 = 0f,
            )
            // top right
            if (cutoutRightX < size.width) {
                val realRightCornerDiameter = if (cutoutRightX <= size.width - cornerRadiusPx) {
                    cornerDiameter
                } else {
                    (size.width - cutoutRightX) * 2
                }
                arcTo(
                    rect = Rect(
                        left = size.width - realRightCornerDiameter,
                        top = 0f,
                        right = size.width.toFloat(),
                        bottom = realRightCornerDiameter
                    ),
                    startAngleDegrees = -90.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            // bottom right
            lineTo(x = size.width.toFloat(), y = size.height.toFloat())
            close()
        }
    }
}


@Composable
fun BottomNavigationBar(navHostController: NavHostController, bottomBarState: Boolean) {
    val withPx = LocalContext.current.resources.displayMetrics.widthPixels

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
//    Toast.makeText(LocalContext.current, currentDestination?.navigatorName,Toast.LENGTH_SHORT).show()

    AnimatedVisibility(
        visible = bottomBarState,
    ) {
        val barShape = BarShape(
            offset = withPx / 2f,
            circleRadius = 30.dp,
            cornerRadius = 0.dp,
            circleGap = 10.dp,
        )

        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = CustomColors.white_ffffff,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CustomColors.grey_f4f4f4
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 30.dp
            ),
            shape = barShape,
            modifier = Modifier
                .background(shape = barShape, color = CustomColors.white_ffffff)
                .shadow(
                    elevation = 30.dp,
                    shape = barShape
                )
        ) {
            NavigationBar(
                modifier =Modifier.background(CustomColors.white_ffffff)
                    .fillMaxWidth()
                    .height(75.dp)
                    .graphicsLayer {
                        shape = barShape
                        clip = true
                    },
                contentColor = CustomColors.white_ffffff,
                containerColor = CustomColors.white_ffffff
            ) {

                BottomNavItems.entries.forEach { screen ->

                    if (screen.route == "") {
                        Spacer(
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight()
                        )
                    } else

                        AddItem(
                            route = screen.route,
                            title = screen.title,
                            icon = screen.icon,
                            iconFocused = screen.iconFocused,
                            navDestination = currentDestination,
                            navController = navHostController
                        )
                }
            }
        }


    }


}

enum class BottomNavItems(
    val route: String,
    val title: String,
    val icon: Int,
    val iconFocused: Int,
) {
    REPORTS(
        route = "home", title = "Home",
        R.drawable.ic_home,
        R.drawable.ic_home
    ),
    HOME(
        route = "search", title = "Search",
        icon = R.drawable.ic_search,
        iconFocused = R.drawable.ic_search
    ),
    NULLS(route = "", title = "", icon = 0, iconFocused = 0),
    SHIFTS(
        route = "feed", title = "Feed",
        R.drawable.ic_feed,
        R.drawable.ic_feed
    ),
    PROFILE(
        route = "profile", title = "Profile",
        icon = R.drawable.ic_profile,
        iconFocused = R.drawable.ic_profile
    ),


}

@Composable
fun RowScope.AddItem(
    route: String,
    title: String,
    icon: Int,
    iconFocused: Int,
    navDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = navDestination?.hierarchy?.any { it.route == route } == true

    NavigationBarItem(
        icon = {
            CustomIcon(
                id = if (navDestination?.hierarchy?.any { it.route == route } == true) iconFocused else icon,
                modifier = Modifier.size(24.dp),
                tint = if (selected) blue_2F52E0 else grey_98999a,
            )
        },
        label = {
            Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                CustomText(
                    text = title,
                    fontColor = if (selected) blue_2F52E0 else grey_98999a,
                )
                if (selected)
                    CustomDividerHoriz(
                        thickness = 3.dp, color = if (selected) blue_2F52E0 else grey_98999a,
                        modifier = Modifier
                            .width(50.dp)
                            .clip(shape = ConstantSizes.itemShapeBottomEndStart_15)
                    )
            }
        },

        selected = selected,
        onClick = {
            navController.navigate(route) {
                popUpTo(
                    navController.graph.startDestinationRoute.toString()
                ) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = blue_2F52E0,
            selectedTextColor = blue_2F52E0,
            unselectedIconColor = grey_98999a,
            indicatorColor = CustomColors.white_ffffff,
            unselectedTextColor = grey_98999a

        ),

        )
}

@Composable
fun CustomDividerHoriz(
    thickness: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .height(thickness)
            .background(color)
    )
}



object ConstantSizes {
    val itemShapeBottomEndStart_15 = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomEnd = 15.dp,
        bottomStart = 15.dp
    )
}


@Composable
fun BottomNavFAB(navHostController: NavHostController){
    Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                shape = CircleShape,
                contentColor = Color.White,
                containerColor = blue_2F52E0,
                onClick = {
                    navHostController.navigate("addWall")
                },
                modifier = Modifier
                    .padding(bottom = 45.dp) // Adjust this to center it inside the cutout
                    .size(50.dp) // Match cutout radius

            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
}


