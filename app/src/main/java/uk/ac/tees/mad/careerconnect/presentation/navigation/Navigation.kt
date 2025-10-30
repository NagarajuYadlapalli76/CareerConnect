package uk.ac.tees.mad.careerconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost






import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import okhttp3.Route

import uk.ac.tees.mad.careerconnect.presentation.auth.AuthScreen
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.careerconnect.presentation.home.HomeViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, homeViewModel: HomeViewModel) {



val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.AuthScreen){

        composable<Routes.AuthScreen>{

            AuthScreen(


            )

        }

    }





}