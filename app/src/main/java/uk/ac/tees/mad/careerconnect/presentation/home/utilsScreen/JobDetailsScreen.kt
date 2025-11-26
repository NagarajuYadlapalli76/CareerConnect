package uk.ac.tees.mad.careerconnect.presentation.home.utilsScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.websocket.Frame
import uk.ac.tees.mad.careerconnect.presentation.home.HomeViewModel
import uk.ac.tees.mad.careerconnect.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    id: String,
    title: String,
    compName: String,
    location: String,
    type: String,
    numApplications: String,
    minSalary: String,
    maxSalary: String,
    description: String,
    publishedDate: String,
    requirements: String,
    onBackClick: () -> Unit = {}, homeViewModel: HomeViewModel,

    ) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var isApplied by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details", color = colorScheme.onBackground) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onBackground
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    homeViewModel.addAppliedJob(id, onResult = { condition, message ->



                        if (condition) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }

                        if (message == "Already applied to this job"){
                            isApplied = true
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                )
            ) {
                Text(if (isApplied) "Successfully Applied" else "Apply Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        },
        containerColor = colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Text(
                text = compName,
                fontSize = 16.sp,
                color = colorScheme.onSurfaceVariant
            )
            Text(
                text = location,
                fontSize = 14.sp,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Job Info Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onBackground
                )
                Text(
                    text = "+$numApplications Applicants",
                    fontSize = 12.sp,
                    color = colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Salary
            Text(
                text = "Salary: $minSalary - $maxSalary / month",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Job Description",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Requirements
            Text(
                text = "Requirements",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = requirements,
                fontSize = 14.sp,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Published Date
            Text(
                text = "Published on: $publishedDate",
                fontSize = 12.sp,
                color = colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }


}







