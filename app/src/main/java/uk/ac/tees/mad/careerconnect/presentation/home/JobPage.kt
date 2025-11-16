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
fun JobPage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
    homeViewModel: HomeViewModel,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "I am Jobs Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
