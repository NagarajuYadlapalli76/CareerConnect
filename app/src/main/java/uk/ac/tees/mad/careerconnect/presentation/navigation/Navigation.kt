package uk.ac.tees.mad.careerconnect.presentation.navigation


import AuthScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Route
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.careerconnect.presentation.auth.LoginScreen
import uk.ac.tees.mad.careerconnect.presentation.home.HomeScreen
import uk.ac.tees.mad.careerconnect.presentation.home.HomeViewModel

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
) {

    val auth = FirebaseAuth.getInstance()


    var currentUser by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            currentUser = it.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    val startDestination = if (currentUser == null) {
        Routes.AuthScreen
    } else {
        Routes.HomeScreen
    }

    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {

        composable<Routes.AuthScreen> {


            AuthScreen(
                authViewModel = authViewModel,
                navController = navController
            )

        }

        composable<Routes.SingInScreen> {


            SignUpScreen(
                authViewModel = authViewModel,
                navController = navController
            )

        }

        composable<Routes.HomeScreen> {


            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel
            )

        }

        composable<Routes.LogInScreen> {


            LoginScreen(
                authViewModel = authViewModel,

                navController = navController
            )

        }



        composable<Routes.PdfViewScreen> {
            val arg = it.toRoute<Routes.PdfViewScreen>()

            LoginScreen(
                authViewModel = authViewModel,

                navController = navController
            )

        }

    }


}