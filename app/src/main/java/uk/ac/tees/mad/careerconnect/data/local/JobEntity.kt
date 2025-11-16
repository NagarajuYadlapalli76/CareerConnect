package uk.ac.tees.mad.careerconnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "jobs")
data class JobEntity(

    @PrimaryKey val id: String,
    val title: String,
    val compName: String,
    val location: String,
    val type: String,
    val numApplications: String,
    val minSalary: String,
    val maxSalary: String,
    val description: String,
    val publishedDate: String,
    val requirements: String

)