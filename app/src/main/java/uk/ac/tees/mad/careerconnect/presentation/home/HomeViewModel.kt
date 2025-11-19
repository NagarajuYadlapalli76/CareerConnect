package uk.ac.tees.mad.careerconnect.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDate
import uk.ac.tees.mad.careerconnect.data.local.JobDao
import uk.ac.tees.mad.careerconnect.data.local.JobEntity
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val jobDao: JobDao) : ViewModel() {

    val allJobs: StateFlow<List<JobEntity>> = jobDao.getAllJobs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    private val searchQuery = MutableStateFlow(SearchFilter())

    fun updateSearch(title: String? = null, location: String? = null, type: String? = null) {
        searchQuery.value = SearchFilter(title, location, type)
    }



    val jobs: StateFlow<List<JobEntity>> = searchQuery

        .flatMapLatest { filter ->

            if (filter.isEmpty()) {
                jobDao.getAllJobs()
            } else {
                jobDao.searchJobs(
                    title = filter.title,
                    location = filter.location,
                    type = filter.type
                )
            }

        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    private fun insertJobs(jobs: List<JobEntity>) {
        viewModelScope.launch {
            jobDao.insertJobs(jobs)
        }
    }


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

    private val db = FirebaseFirestore.getInstance()

    fun addSampleJobsToFirestoreSuspend() {
        val jobs = sampleJobs

        viewModelScope.launch {
            jobs.forEach { job ->
                try {
                    db.collection("jobs")
                        .document(job.id)
                        .set(job)
                        .await()

                } catch (e: Exception) {
                    println("Error adding job ${job.title}: $e")
                }
            }
        }
    }


}


data class GetJobInfo(
    val id: String = "",
    val title: String = " ",
    val compName: String = "",
    val location: String = "",
    val type: String = "",
    val numApplications: String = "",
    val minSalary: String = "",
    val maxSalary: String = "",
    val description: String = "",
    val publishedDate: String = "",
    val requirements: String = "",
)


data class JobInfo(
    val id: String = UUID.randomUUID().toString(),
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

data class SearchFilter(
    val title: String? = null,
    val location: String? = null,
    val type: String? = null,
) {
    fun isEmpty(): Boolean {
        return title.isNullOrBlank() && location.isNullOrBlank() && type.isNullOrBlank()
    }
}





val sampleJobs = listOf(
    JobInfo(
        title = "UI Designer Intern",
        compName = "DesignHive Ltd.",
        location = "London, UK",
        type = "Internship",
        numApplications = "145",
        minSalary = "45k",
        maxSalary = "60k",
        description = "We are seeking a creative UI Designer intern to assist in crafting user-friendly interfaces. "
                + "You will work with senior designers to help transform requirements into intuitive designs. "
                + "The role involves supporting the design system and improving overall user journeys.",
        publishedDate = "2025-09-12",
        requirements = "Figma · Prototyping · UI Design Systems · Adobe XD · Strong Portfolio"
    ),
    JobInfo(
        title = "Web Developer Intern",
        compName = "TechWorks Solutions",
        location = "Manchester, UK",
        type = "Internship",
        numApplications = "98",
        minSalary = "40k",
        maxSalary = "55k",
        description = "Join our web development team as an intern to assist in building modern web applications. "
                + "You’ll collaborate with developers on responsive UI, integrating APIs, and optimizing performance. "
                + "Contribute to testing and deployment of scalable features.",
        publishedDate = "2025-08-27",
        requirements = "HTML · CSS · JavaScript · React · Node.js"
    ),
    JobInfo(
        title = "Data Science Intern",
        compName = "Insight Analytics",
        location = "Bristol, UK",
        type = "Internship",
        numApplications = "112",
        minSalary = "50k",
        maxSalary = "65k",
        description = "Work with our analytics team to analyze datasets and assist in designing predictive models. "
                + "Support experiments and data-driven strategies while learning from experienced data scientists. "
                + "You will also help visualize data and communicate findings clearly.",
        publishedDate = "2025-07-18",
        requirements = "Python · Machine Learning · SQL · Data Visualization · Statistics"
    ),
    JobInfo(
        title = "Android Developer Intern",
        compName = "MobileFirst Apps",
        location = "Leeds, UK",
        type = "Internship",
        numApplications = "87",
        minSalary = "42k",
        maxSalary = "58k",
        description = "Assist in developing Android applications with Kotlin and Jetpack Compose. "
                + "Collaborate with the development team to implement features and ensure smooth app performance. "
                + "You will also learn best practices in mobile development and QA processes.",
        publishedDate = "2025-09-05",
        requirements = "Kotlin · Jetpack Compose · Firebase · Git · MVVM"
    ),
    JobInfo(
        title = "Flutter Developer Intern",
        compName = "AppVision Ltd.",
        location = "London, UK",
        type = "Internship",
        numApplications = "102",
        minSalary = "48k",
        maxSalary = "60k",
        description = "Work on cross-platform mobile applications using Flutter and Dart. "
                + "You’ll assist with UI/UX decisions, write clean code, and help ensure app scalability. "
                + "Also participate in debugging and improving code quality through peer reviews.",
        publishedDate = "2025-06-30",
        requirements = "Flutter · Dart · REST APIs · Firebase · State Management"
    ),
    JobInfo(
        title = "Mobile Developer Intern",
        compName = "NextGen Mobile",
        location = "Birmingham, UK",
        type = "Internship",
        numApplications = "95",
        minSalary = "50k",
        maxSalary = "65k",
        description = "Support the development of both native and cross-platform mobile apps. "
                + "Learn to work on apps that scale across devices, ensuring excellent performance and user experience. "
                + "Collaborate with product and design teams on end-to-end solutions.",
        publishedDate = "2025-08-09",
        requirements = "Kotlin · Swift · Flutter · REST APIs · Agile Practices"
    ),
    JobInfo(
        title = "UX Designer Intern",
        compName = "CreativeMind Studio",
        location = "London, UK",
        type = "Internship",
        numApplications = "88",
        minSalary = "42k",
        maxSalary = "55k",
        description = "Assist in crafting seamless user experiences across platforms. "
                + "Participate in user research, wireframing, and prototype creation. "
                + "Work closely with product and engineering teams under supervision of senior UX designers.",
        publishedDate = "2025-09-01",
        requirements = "User Research · Wireframing · Prototyping · Usability Testing · Figma"
    ),
    JobInfo(
        title = "Cloud Engineer Intern",
        compName = "SkyNet Solutions",
        location = "Manchester, UK",
        type = "Internship",
        numApplications = "110",
        minSalary = "55k",
        maxSalary = "70k",
        description = "Assist in designing and managing cloud systems on AWS. "
                + "Learn to implement CI/CD pipelines, maintain infrastructure security, and monitor uptime. "
                + "Get hands-on experience with cloud technologies and troubleshooting.",
        publishedDate = "2025-08-15",
        requirements = "AWS · Docker · Kubernetes · Terraform · CI/CD"
    ),
    JobInfo(
        title = "Game Designer Intern",
        compName = "PlayForge Studios",
        location = "Liverpool, UK",
        type = "Internship",
        numApplications = "72",
        minSalary = "45k",
        maxSalary = "55k",
        description = "Help design engaging gameplay and immersive worlds. "
                + "Support mechanics, level design, and storyboarding tasks under senior designers. "
                + "Balance creativity with feasibility in a collaborative team environment.",
        publishedDate = "2025-07-22",
        requirements = "Unity · Game Mechanics · Storyboarding · Level Design · C# Basics"
    ),
    JobInfo(
        title = "AI Engineer Intern",
        compName = "NeuroTech AI",
        location = "Cambridge, UK",
        type = "Internship",
        numApplications = "65",
        minSalary = "60k",
        maxSalary = "75k",
        description = "Assist in developing and optimizing machine learning models for production. "
                + "Work alongside senior engineers on AI systems, deep learning, and data pipelines. "
                + "Support research and model fine-tuning for innovative projects.",
        publishedDate = "2025-09-10",
        requirements = "Python · TensorFlow · PyTorch · Data Engineering · MLOps"
    ),
    JobInfo(
        title = "Backend Developer Intern",
        compName = "CodeCraft Ltd.",
        location = "Bristol, UK",
        type = "Internship",
        numApplications = "78",
        minSalary = "44k",
        maxSalary = "60k",
        description = "Assist in building robust backend systems using Node.js and Python. "
                + "Work on database queries, API development, and integration testing under supervision. "
                + "Learn to maintain scalable and secure backend services.",
        publishedDate = "2025-07-30",
        requirements = "Node.js · Python · SQL · REST APIs · Git"
    ),
    JobInfo(
        title = "Frontend Developer Intern",
        compName = "PixelTech Ltd.",
        location = "Leeds, UK",
        type = "Internship",
        numApplications = "83",
        minSalary = "42k",
        maxSalary = "58k",
        description = "Support building responsive front-end applications using React and CSS frameworks. "
                + "Collaborate with designers and backend developers to implement features and styles. "
                + "Learn testing, debugging, and optimization of UI components.",
        publishedDate = "2025-08-05",
        requirements = "React · JavaScript · HTML · CSS · Git"
    ),
    JobInfo(
        title = "Marketing Intern",
        compName = "BrandBoost",
        location = "London, UK",
        type = "Internship",
        numApplications = "90",
        minSalary = "38k",
        maxSalary = "50k",
        description = "Assist the marketing team with campaigns, content creation, and social media engagement. "
                + "Learn analytics and marketing tools while supporting brand promotion tasks. "
                + "Collaborate with teams to optimize strategies.",
        publishedDate = "2025-09-03",
        requirements = "Marketing · Social Media · Content Creation · Analytics · Communication"
    ),
    JobInfo(
        title = "QA Tester Intern",
        compName = "TestLab Ltd.",
        location = "Manchester, UK",
        type = "Internship",
        numApplications = "72",
        minSalary = "40k",
        maxSalary = "52k",
        description = "Support QA engineers in testing web and mobile applications. "
                + "Execute test cases, report bugs, and assist in creating test documentation. "
                + "Learn QA processes and automation basics.",
        publishedDate = "2025-08-18",
        requirements = "Manual Testing · QA Tools · Bug Reporting · Automation Basics · Git"
    ),
    JobInfo(
        title = "Graphic Designer Intern",
        compName = "CreativeSpark",
        location = "Liverpool, UK",
        type = "Internship",
        numApplications = "60",
        minSalary = "35k",
        maxSalary = "45k",
        description = "Assist in creating graphics for digital and print media. "
                + "Work with senior designers on campaigns, visual content, and branding projects. "
                + "Learn tools like Photoshop and Illustrator.",
        publishedDate = "2025-07-25",
        requirements = "Adobe Photoshop · Illustrator · Creativity · Typography · Communication"
    ),
    JobInfo(
        title = "Product Manager Intern",
        compName = "VisionaryTech",
        location = "Cambridge, UK",
        type = "Internship",
        numApplications = "55",
        minSalary = "45k",
        maxSalary = "60k",
        description = "Support product managers in planning, tracking, and coordinating projects. "
                + "Assist in meetings, documentation, and research for product features. "
                + "Learn product lifecycle and agile methodologies.",
        publishedDate = "2025-09-07",
        requirements = "Agile · Communication · Planning · Documentation · Research"
    )
)
