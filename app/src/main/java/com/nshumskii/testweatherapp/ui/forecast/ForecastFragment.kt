package com.nshumskii.testweatherapp.ui.forecast

import androidx.fragment.app.viewModels
import com.nshumskii.testweatherapp.R
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.databinding.FragmentCitiesBinding
import com.nshumskii.testweatherapp.databinding.FragmentForecastBinding
import com.nshumskii.testweatherapp.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastFragment :
    BaseFragment<ForecastViewModel, FragmentForecastBinding>(R.layout.fragment_forecast) {

    override val viewModel: ForecastViewModel by viewModels()

    override fun createBinding(): FragmentForecastBinding = FragmentForecastBinding.bind(requireView())

    lateinit var dailyAdapter: DailyAdapter

    override fun setupViews() {
        arguments?.getSerializable("coord")?.let { coord ->
            viewModel.getForecast(coord as Coord)
        }

        dailyAdapter = DailyAdapter()
        binding?.dailyRecycler?.adapter = dailyAdapter
    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.forecasts.observe(viewLifecycleOwner, { response ->
            dailyAdapter.submitList(response.daily)
        })
    }

}