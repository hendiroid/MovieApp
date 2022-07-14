package com.fawry.movieapptask.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.fawry.movieapptask.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException


@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFragment<V : ViewDataBinding>
constructor(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

     lateinit var uiController: UIController
     lateinit var dataBindingView: V

    private fun init(inflater: LayoutInflater, container: ViewGroup, layoutId: Int) {
        dataBindingView = DataBindingUtil.inflate(inflater, layoutId, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        init(inflater, container!!, layoutRes)

        return dataBindingView.root
    }


    fun displayToolbarTitle(textView: TextView, title: String?, useAnimation: Boolean) {
        if (title != null) {
            showToolbarTitle(textView, title, useAnimation)
        } else {
            hideToolbarTitle(textView, useAnimation)
        }
    }

    private fun hideToolbarTitle(textView: TextView, animation: Boolean) {
        if (animation) {
            textView.fadeOut(
                object : TodoCallback {
                    override fun execute() {
                        textView.text = ""
                    }
                }
            )
        } else {
            textView.text = ""
            textView.gone()
        }
    }

    private fun showToolbarTitle(
        textView: TextView,
        title: String,
        animation: Boolean
    ) {
        textView.text = title
        if (animation) {
            textView.fadeIn()
        } else {
            textView.visible()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUIController(null) // null in production
    }

    fun setUIController(mockController: UIController?) {

        // TEST: Set interface from mock
        if (mockController != null) {
            this.uiController = mockController
        } else { // PRODUCTION: if no mock, get from context
            activity?.let {
                if (it is MainActivity) {
                    try {
                        uiController = context as UIController
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

















