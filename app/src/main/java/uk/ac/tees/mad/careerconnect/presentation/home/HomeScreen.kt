package uk.ac.tees.mad.careerconnect.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
    homeViewModel: HomeViewModel,
    ) {

    val navItems = listOf(
        NavItems(
            "Applications",
            filledIcon = Icons.Filled.Description,
            outlinedIcon = Icons.Outlined.Description
        ),

        NavItems(
            "Jobs",
            filledIcon = Icons.Filled.Work,
            outlinedIcon = Icons.Outlined.WorkOutline
        ),
        NavItems(
            "Profile",
            filledIcon = Icons.Filled.AccountCircle,
            outlinedIcon = Icons.Outlined.AccountCircle
        )
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }

    Scaffold(modifier.fillMaxSize(), bottomBar = {
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color(0xFF3B6CFF)
        ) {

            navItems.fastForEachIndexed() { index, navItem ->
                val isSelected = selectedIndex == index
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) navItem.filledIcon else navItem.outlinedIcon,
                            contentDescription = null,
                            tint = Color.Black

                        )
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            modifier = Modifier.offset(y = (-4).dp),
                            color =  Color.Black
                        )
                    }
                )
            }
        }
    }) { innerPadding ->

        ContentScreen(
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel,
            homeViewModel = homeViewModel,
            modifier = Modifier.padding(innerPadding)
        )

    }
}


data class NavItems(val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector)


@Composable
fun ContentScreen(
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    modifier: Modifier,
) {
    MaterialTheme {
        when (selectedIndex) {



            0 ->{
                ApplicationsPage(
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    navController = navController,
                )
            }
            1 -> JobPage(
                authViewModel = authViewModel,
                navController = navController,
                homeViewModel = homeViewModel
            )


            2 -> ProfilePage(
                authViewModel = authViewModel


            )


        }
    }
}