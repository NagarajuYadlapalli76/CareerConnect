package uk.ac.tees.mad.careerconnect.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.careerconnect.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsPage(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    var isLoading by rememberSaveable { mutableStateOf(true) }


    LaunchedEffect(Unit) {

        homeViewModel.getAppliedJobs()
    }

    val jobs by homeViewModel.appliedJobs.collectAsState()


    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Application Status",
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.background
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFF3B6CFF),
                    titleContentColor = Color.White
                )
            )
        }, floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.SavedJobSScreen) },
                containerColor = Color(0xFF3B6CFF),
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 100.dp)) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Save",
                    tint =  MaterialTheme.colorScheme.background
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp), contentAlignment = Alignment.Center
        ) {
            if (jobs.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),

                    ) {
                    items(jobs) {
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
                            },
                        )
                    }
                    item(1) { Spacer(modifier = Modifier.height(50.dp)) }


                }


            } else {
                if (jobs.isEmpty()) {

                    Text("No Application Found")

                } else {


                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)

                }

            }
        }
    }


}