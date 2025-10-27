package com.wasterec.app.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.wasterec.app.R
import com.wasterec.app.model.Destination.Home
import com.wasterec.app.model.Destination.Info
import com.wasterec.app.model.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wasterec.app.ui.theme.dark_teal
import com.wasterec.app.ui.theme.light_teal
import com.wasterec.app.ui.theme.text_secondary
import com.wasterec.app.ui.theme.divider_gray
import com.wasterec.app.ui.theme.app_background

@Composable
fun MainView(
    navController : NavHostController
){
    val entries = arrayOf(
        Menu("Home", Home, R.drawable.baseline_home_24),
        Menu("Info", Info, R.drawable.baseline_add_location_24)
    )

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            Column {
                Divider(thickness = 1.dp, color = divider_gray)

                NavigationBar(
                    containerColor = app_background,
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                            },
                            icon = {
                                Icon(
                                    painterResource(destination.icon),
                                    contentDescription = destination.label,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            label = { Text(destination.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = dark_teal,
                                selectedTextColor = dark_teal,
                                unselectedIconColor = text_secondary,
                                unselectedTextColor = text_secondary,
                                indicatorColor = light_teal
                            )
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        if (selectedTab == 0){
            HomeView(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        } else if (selectedTab == 1) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Halaman Info")
            }
        }
    }
}

