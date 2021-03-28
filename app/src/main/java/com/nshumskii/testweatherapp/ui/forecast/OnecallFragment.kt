package com.nshumskii.testweatherapp.ui.forecast

import androidx.fragment.app.viewModels
import com.nshumskii.testweatherapp.R
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.databinding.FragmentOnecallBinding
import com.nshumskii.testweatherapp.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnecallFragment :
    BaseFragment<OnecallViewModel, FragmentOnecallBinding>(R.layout.fragment_onecall) {

    override val viewModel: OnecallViewModel by viewModels()

    override fun createBinding(): FragmentOnecallBinding =
        FragmentOnecallBinding.bind(requireView())

    lateinit var dailyAdapter: DailyAdapter

    override fun setupViews() {
        arguments?.getParcelable<CurrentWeatherEntity>("weather")?.let { weather ->
            viewModel.getOnecall(weather.coord)
        }

        dailyAdapter = DailyAdapter()
        binding?.dailyRecycler?.adapter = dailyAdapter
    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.onecall.observe(viewLifecycleOwner, { response ->
            dailyAdapter.submitList(response.daily)
        })
    }

}