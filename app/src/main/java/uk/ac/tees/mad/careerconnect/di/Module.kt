package uk.ac.tees.mad.careerconnect.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.careerconnect.data.repoImpl.RepositoryImpl
import uk.ac.tees.mad.careerconnect.domain.repo.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {




    @Provides
    @Singleton
    fun bindWeatherRepository (): Repository{
        return RepositoryImpl()
    }

}


