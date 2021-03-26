package com.nshumskii.testweatherapp.utils.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.nshumskii.testweatherapp.R

abstract class BaseFragment<ViewModel : BaseViewModel, Binding : ViewBinding>(
    layoutID: Int
) : Fragment(layoutID) {

    protected abstract val viewModel: ViewModel

    protected var binding: Binding? = null

    private var progressBar: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = createBinding()

        setupBaseObservers()
        setupBaseViews()

        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupBaseViews() {
        progressBar = requireView().findViewById(R.id.progress)
    }

    private fun setupBaseObservers() {
        viewModel.progress.observe(viewLifecycleOwner, { event ->
            if (!event.hasBeenHandled) {
                event.getContentIfNotHandled()?.let { visible ->
                    progressBar?.visibility = if (visible) View.VISIBLE else View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, { event ->
            if (!event.hasBeenHandled) {
                event.getContentIfNotHandled()?.let { message ->
                    showError(message)
                }
            }
        })
    }

    abstract fun createBinding(): Binding

    abstract fun setupViews()

    abstract fun setupListeners()

    abstract fun setupObservers()

    fun showError(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}