package uk.ac.tees.mad.careerconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import okhttp3.Route
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.careerconnect.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
    homeViewModel: HomeViewModel,
) {

    val jobs by homeViewModel.jobs.collectAsStateWithLifecycle()
    var searchText by rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var isProgress by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Career Connect", maxLines = 1, color = MaterialTheme.colorScheme.background) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFF3B6CFF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = searchText,
                onValueChange = {

                    searchText = it
                    if (searchText.isBlank()) {
                        homeViewModel.updateSearch(null)
                    } else {
                        homeViewModel.updateSearch(searchText)
                    }

                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {
                itemsIndexed(jobs) { index, it ->
                    JobCard(
                        title = it.title,
                        company = it.compName,
                        location = it.location,
                        applicants = it.numApplications,
                        minSal = it.minSalary,
                        maxSal = it.minSalary,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        tag = it.type,
                        onClick = {
                            navController.navigate(
                                Routes.JobDetailScreen(
                                    id = it.id,
                                    title = it.title,
                                    compName = it.compName,
                                    location = it.location,
                                    type = it.type,
                                    numApplications = it.numApplications,
                                    minSalary = it.minSalary,
                                    maxSalary = it.maxSalary,
                                    description = it.description,
                                    publishedDate = it.publishedDate,
                                    requirements = it.requirements,

                                    )
                            )
                        }
                    )

                    if (index == jobs.lastIndex) {

                        LaunchedEffect(Unit) {
                            isProgress = true
                            delay(2000)
                            homeViewModel.loadMore()
                            isProgress = false
                        }

                    }

                }



                item(1) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        if (isProgress) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                        }

                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }


            }


        }
    }


}


@Composable
fun JobCard(
    title: String,
    company: String,
    location: String,
    tag: String,
    applicants: String,
    minSal: String,
    maxSal: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,

    ) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Company & Location
            Text(
                text = company,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = location,
                fontSize = 12.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Row {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE3E3F0), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .padding(end = 4.dp)
                ) {
                    Text(tag, fontSize = 12.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Applicants + Salary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+$applicants Applicants",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Text(
                    text = "$minSal - $maxSal /month",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}


//item(0) {
//
//    Text("Best Job For you ")
//
//    LazyRow (modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 10.dp, vertical = 0.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//    ){
//        items(jobs){
//            JobCard(
//                title = it.title,
//                company = it.compName,
//                location = it.location,
//                applicants = it.numApplications,
//                minSal = it.minSalary,
//                maxSal = it.minSalary,
//                modifier = Modifier.fillParentMaxWidth()  ,
//                tag = it.type,
//
//                )
//        }
//
//    }
//}