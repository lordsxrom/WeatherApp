package com.nshumskii.testweatherapp.ui.forecast

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        setHasOptionsMenu(true)

        arguments?.getParcelable<CurrentWeatherEntity>("weather")?.let { weather ->
            viewModel.getOnecall(weather.coord)
        }

        dailyAdapter = DailyAdapter()
        binding?.dailyRecycler?.adapter = dailyAdapter
    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.forecast.observe(viewLifecycleOwner, { list ->
            dailyAdapter.submitList(list)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_onecall, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_daily -> {
                viewModel.onForecastTypeSelected(ForecastType.TYPE_DAILY)
                true
            }
            R.id.action_show_hourly -> {
                viewModel.onForecastTypeSelected(ForecastType.TYPE_HOURLY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


}