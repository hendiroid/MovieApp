package com.fawry.movieapptask.ui.movies

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fawry.movieapptask.ui.BaseFragment
import com.fawry.movieapptask.R
import com.fawry.movieapptask.business.models.Item
import com.fawry.movieapptask.databinding.FragmentMoviesBinding
import com.fawry.movieapptask.ui.details.MOVIE_DETAIL_SELECTED_BUNDLE_KEY
import com.fawry.movieapptask.util.SnackBarUtils
import com.fawry.movieapptask.util.ViewState
import com.fawry.movieapptask.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MoviesFragment :
    BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies), MoviesListAdapter.Interaction {

    private var listAdapter: MoviesListAdapter? = null
    private val viewModel: MoviesViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        viewModel.getMovies()
        setupRecyclerView()
    }

    private fun subscribeObservers() {
        viewModel.moviesViewState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> displayProgressBar(true)
                is ViewState.Success -> {
                    displayProgressBar(false)
                    listAdapter?.submitList(it.data)
                    listAdapter?.notifyDataSetChanged()
                }
                is ViewState.Error -> {
                    displayProgressBar(false)
                    SnackBarUtils.showErrorSnackBar(context!!, view!!.rootView, it.message.message)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        dataBindingView.movieListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            listAdapter = MoviesListAdapter(
                this@MoviesFragment,
                viewLifecycleOwner
            )

            adapter = listAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Item) {
        printLogD("listadapter", "id: ${item.id}")
        navigateToDetailFragment(item.id)
    }

    override fun restoreListPosition() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        dataBindingView.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun navigateToDetailFragment(id: Int) {
        val bundle = bundleOf(MOVIE_DETAIL_SELECTED_BUNDLE_KEY to id)
        findNavController().navigate(
            R.id.action_movie_list_fragment_to_movieDetailFragment,
            bundle
        )
    }
}