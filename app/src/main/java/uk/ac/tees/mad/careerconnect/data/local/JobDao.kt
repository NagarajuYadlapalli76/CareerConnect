package uk.ac.tees.mad.careerconnect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Query("SELECT * FROM jobs")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Upsert()
    suspend fun insertJobs(jobs: List<JobEntity>)

    // Search jobs by title, location, and type
    @Query("""
    SELECT * FROM jobs
    WHERE (:title IS NULL OR :title = '' OR LOWER(title) LIKE '%' || LOWER(:title) || '%')
    AND (:location IS NULL OR :location = '' OR LOWER(location) LIKE '%' || LOWER(:location) || '%')
    AND (:type IS NULL OR :type = '' OR LOWER(type) LIKE '%' || LOWER(:type) || '%')
""")
    fun searchJobs(
        title: String? = null,
        location: String? = null,
        type: String? = null
    ): Flow<List<JobEntity>>
}