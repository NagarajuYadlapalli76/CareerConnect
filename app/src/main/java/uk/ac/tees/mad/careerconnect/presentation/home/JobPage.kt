
package uk.ac.tees.mad.careerconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel

@Composable
fun JobPage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
    homeViewModel: HomeViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
        )

        Spacer(modifier = Modifier.height(24.dp))




        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item() {
                Text(
                    "Suggested Jobs",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            items(1) { rowIndex ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Each row has 5 JobCards
                    items(5) { cardIndex ->
                        JobCard(
                            title = "UI Designer",
                            company = "BrioSoft Solutions",
                            location = "New York, USA",
                            tags = listOf("Full-Time", "Remote", "Internship"),
                            applicants = 322,
                            salaryRange = "$42k - $48k",
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                    }
                }
            }


            items(12) { rowIndex ->


                JobCard(
                    title = "UI Designer",
                    company = "BrioSoft Solutions",
                    location = "New York, USA",
                    tags = listOf("Full-Time", "Remote", "Internship"),
                    applicants = 322,
                    salaryRange = "$42k - $48k",
                    modifier = Modifier.padding(horizontal = 6.dp)
                )


            }


        }


    }
}


@Composable
fun JobCard(
    title: String,
    company: String,
    location: String,
    tags: List<String>,
    applicants: Int,
    salaryRange: String,
    modifier: Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Title + Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF0D0DFD), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔖", fontSize = 12.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(company, color = Color.Gray, fontSize = 14.sp)
            Text(location, color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Row {
                tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE3E3F0), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .padding(end = 4.dp)
                    ) {
                        Text(tag, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Applicants + Salary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box{
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(

                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .border(1.dp, Color.White, CircleShape).zIndex(1f)
                        )

                        Box(

                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .border(1.dp, Color.White, CircleShape).offset(x = (-12).dp)
                                .zIndex(2f)
                        )


                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .border(1.dp, Color.White, CircleShape).offset(x = (-12).dp)
                                .zIndex(2f)
                        )
                        Text(
                            "+$applicants Applicants",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }


                Text("$salaryRange /month", fontWeight = FontWeight.Bold)
            }
        }
    }
}
