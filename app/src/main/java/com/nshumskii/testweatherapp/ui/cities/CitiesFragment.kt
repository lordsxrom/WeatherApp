package com.nshumskii.testweatherapp.ui.cities

import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.nshumskii.testweatherapp.R
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.databinding.FragmentCitiesBinding
import com.nshumskii.testweatherapp.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class CitiesFragment :
    BaseFragment<CitiesViewModel, FragmentCitiesBinding>(R.layout.fragment_cities) {

    override val viewModel: CitiesViewModel by viewModels()

    override fun createBinding(): FragmentCitiesBinding = FragmentCitiesBinding.bind(requireView())

    lateinit var citiesAdapter: CitiesAdapter

    override fun setupViews() {
        setHasOptionsMenu(true)
        getLocation()

        val onItemClickListener = object : CitiesAdapter.OnItemClickListener {
            override fun onItemClick(weather: CurrentWeatherEntity) {
                findNavController().navigate(
                    R.id.action_citiesFragment_to_forecastFragment,
                    Bundle().apply { putSerializable("coord", weather.coord) }
                )
            }
        }

        citiesAdapter = CitiesAdapter(onItemClickListener)
        binding?.citiesRecycler?.let { recycler ->
            recycler.adapter = citiesAdapter
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val currentWeather = citiesAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.removeCity(currentWeather)
                }
            }).attachToRecyclerView(recycler)
        }
    }

    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.currentWeather.observe(viewLifecycleOwner, { currentWeather ->
            binding?.icon?.let { holder ->
                Glide.with(requireContext())
                    .load("http://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@4x.png")
                    .into(holder)
            }
            binding?.city?.text = currentWeather.name
            binding?.weatherDesc?.text = currentWeather.weather[0].description
            binding?.currentTemp?.text = "${currentWeather.main.temp.roundToInt()} ℃"
            binding?.hightLowTemp?.text =
                "H:${currentWeather.main.temp_max} L:${currentWeather.main.temp_min}"
        })

        viewModel.citiesWeathers.observe(viewLifecycleOwner, { list ->
            citiesAdapter.submitList(list)
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weathersEvent.collect { event ->
                when(event) {
                    is CitiesViewModel.WeathersEvent.ShowUndoDeleteWeatherMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.weather)
                            }.show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_cities, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { text ->
                    if (text.isNotEmpty()) {
                        viewModel.addCity(text)
                        return true
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            task.result?.let { location ->
                try {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val adresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    viewModel.getCurrentWeather(adresses[0].locality)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}