package com.fawry.movieapptask.di

import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object FragmentModule {

    @OptIn(FlowPreview::class)
    @Singleton
    @Provides
    fun provideMoviesFragmentFactory(): FragmentFactory {
        return MoviesFragmentFactory()
    }

}





