package uk.ac.tees.mad.careerconnect.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDate
import uk.ac.tees.mad.careerconnect.data.local.JobDao
import uk.ac.tees.mad.careerconnect.data.local.JobEntity

import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(private  val jobDao: JobDao) : ViewModel() {

    val allJobs: StateFlow<List<JobEntity>> = jobDao.getAllJobs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun insertJobs(jobs: List<JobEntity>) {
        viewModelScope.launch {
            jobDao.insertJobs(jobs)
        }
    }
    // Fetch jobs from Firestore and store locally
    fun fetchJobsFromFirestore() {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("jobs")
                    .get()
                    .await()

                val jobs = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(GetJobInfo::class.java)?.let { info ->
                        JobEntity(
                            id = info.id,
                            title = info.title,
                            compName = info.compName,
                            location = info.location,
                            type = info.type,
                            numApplications = info.numApplications,
                            minSalary = info.minSalary,
                            maxSalary = info.maxSalary,
                            description = info.description,
                            publishedDate = info.publishedDate,
                            requirements = info.requirements
                        )
                    }
                }

                insertJobs(jobs)

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching jobs: ${e.message}")
            }
        }
    }






}




data class GetJobInfo(
    val id: String  = "",
    val title: String = " ",
    val compName: String = "",
    val location: String = "",
    val type: String  = "",
    val numApplications: String = "",
    val minSalary: String = "",
    val maxSalary: String ="",
    val description: String = "",
    val publishedDate: String = "",
    val requirements: String="",
)



data class JobInfo(
    val id: String ,
    val title: String,
    val compName: String,
    val location: String,
    val type: String,
    val numApplications: String,
    val minSalary: String,
    val maxSalary: String,
    val description: String,
    val publishedDate: String,
    val requirements: String,
)




//    private val db = FirebaseFirestore.getInstance()
//
//    fun addSampleJobsToFirestoreSuspend() {
//        val jobs = sampleJobs
//
//        viewModelScope.launch {
//            jobs.forEach { job ->
//                try {
//                    db.collection("jobs")
//                        .document(job.id)
//                        .set(job)
//                        .await()
//
//                } catch (e: Exception) {
//                    println("Error adding job ${job.title}: $e")
//                }
//            }
//        }
//    }
