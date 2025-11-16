package uk.ac.tees.mad.careerconnect.di

import android.app.Application
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.careerconnect.data.local.AppDatabase
import uk.ac.tees.mad.careerconnect.data.local.JobDao

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db").build()


    @Provides
    fun provideJobDao(db: AppDatabase): JobDao = db.jobDao()



}


