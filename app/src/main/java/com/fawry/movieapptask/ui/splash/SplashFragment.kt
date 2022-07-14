package com.fawry.movieapptask.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fawry.movieapptask.ui.BaseFragment
import com.fawry.movieapptask.R
import com.fawry.movieapptask.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@OptIn(FlowPreview::class)
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashFragment() : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_movieListFragment)
        }, 2000)
    }

}