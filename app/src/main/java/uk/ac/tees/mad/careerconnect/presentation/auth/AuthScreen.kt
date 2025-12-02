
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.careerconnect.R
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.careerconnect.presentation.navigation.Routes

@Composable
fun AuthScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Content Column (top/middle)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Heading Text
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Medium
                        )
                    ) { append("Finding ") }

                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF4F46E5),
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Your Perfect Career") }

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Medium
                        )
                    ) { append(" Path Starts Here!") }
                },
                style = MaterialTheme.typography.headlineSmall, // modern font
                modifier = Modifier.padding(horizontal = 12.dp),
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add an image from drawable after heading
            Image(
                painter = painterResource(id = R.drawable.auth_one), // replace with your drawable
                contentDescription = "Career illustration",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                alignment = Alignment.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Description
            Text(
                text = "Explore jobs that match your passion, skills, and goals — all in one place.",
                style = MaterialTheme.typography.bodyLarge.copy( // modern style
                    color = Color.Gray,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        // Bottom Column for button + sign in text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* TODO */
                navController.navigate(Routes.SingInScreen)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Let's Get Started",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }



            TextButton(onClick = {

                navController.navigate(Routes.LogInScreen)
            }) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground // "Already have an account?" stays black
                            )
                        ) { append("Already have an account? ") }

                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF4F46E5), // Blue for "Log in"
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) { append("Log in") }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

        }

            Spacer(modifier = Modifier.height(32.dp))
        }



}

