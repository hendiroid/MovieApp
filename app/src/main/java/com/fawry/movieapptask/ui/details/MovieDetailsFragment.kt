package com.fawry.movieapptask.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fawry.movieapptask.R
import com.fawry.movieapptask.business.models.Item
import com.fawry.movieapptask.business.models.MovieDataModel
import com.fawry.movieapptask.databinding.FragmentMovieDetailsBinding
import com.fawry.movieapptask.ui.BaseFragment
import com.fawry.movieapptask.ui.movies.MoviesViewModel
import com.fawry.movieapptask.util.SnackBarUtils
import com.fawry.movieapptask.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


const val MOVIE_DETAIL_SELECTED_BUNDLE_KEY = "selectedMovie"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(R.layout.fragment_movie_details) {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            (args.getInt(MOVIE_DETAIL_SELECTED_BUNDLE_KEY)).let { movieId ->
                viewModel.getMovieDetails(movieId)
            }
        }
        setupOnBackPressDispatcher()
        dataBindingView.model = MovieDataModel()
        subscribeObservers()

    }

    private fun subscribeObservers() {
        viewModel.movieDetailsViewState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> displayProgressBar(true)
                is ViewState.Success -> {
                    displayProgressBar(false)
                    dataBindingView.model = it.data
                    dataBindingView.rate = it.data.vote_average / 2
                }
                is ViewState.Error -> {
                    displayProgressBar(false)
                    SnackBarUtils.showErrorSnackBar(context!!, view!!.rootView, it.message.message)
                }
            }
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        dataBindingView.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}