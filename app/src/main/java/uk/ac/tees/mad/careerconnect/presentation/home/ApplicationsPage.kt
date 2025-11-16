package uk.ac.tees.mad.careerconnect.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel

@Composable
fun ApplicationsPage(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "I am Applications Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}