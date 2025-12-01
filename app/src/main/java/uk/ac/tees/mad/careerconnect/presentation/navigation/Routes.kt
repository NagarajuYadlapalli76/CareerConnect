package uk.ac.tees.mad.careerconnect.presentation.navigation


sealed class Routes {
    @kotlinx.serialization.Serializable
    data object AuthScreen

    @kotlinx.serialization.Serializable
    data object SingInScreen

    @kotlinx.serialization.Serializable
    data object LogInScreen

    @kotlinx.serialization.Serializable
    data object HomeScreen

    @kotlinx.serialization.Serializable
    data class PdfViewScreen(val userName: String, val pdfUrl: String)

    @kotlinx.serialization.Serializable
    data class JobDetailScreen(
        val id: String,
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

    @kotlinx.serialization.Serializable
    data object SavedJobSScreen


}


