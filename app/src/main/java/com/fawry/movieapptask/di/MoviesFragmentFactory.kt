package com.fawry.movieapptask.di

import androidx.fragment.app.FragmentFactory
import com.fawry.movieapptask.ui.details.MovieDetailsFragment
import com.fawry.movieapptask.ui.movies.MoviesFragment
import com.fawry.movieapptask.ui.splash.SplashFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MoviesFragmentFactory
@Inject
constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            MovieDetailsFragment::class.java.name -> {
                val fragment = MovieDetailsFragment()
                fragment
            }

            MoviesFragment::class.java.name -> {
                val fragment = MoviesFragment()
                fragment
            }

            SplashFragment::class.java.name -> {
                val fragment = SplashFragment()
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}