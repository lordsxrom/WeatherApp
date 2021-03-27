package com.nshumskii.testweatherapp.ui.cities

import android.location.Geocoder
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.nshumskii.testweatherapp.R
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.databinding.FragmentCitiesBinding
import com.nshumskii.testweatherapp.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
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
        getLocation()

        val onItemClickListener = object : CitiesAdapter.OnItemClickListener {
            override fun onItemClick(weather: CurrentWeatherEntity) {
                findNavController().navigate(
                    R.id.action_citiesFragment_to_forecastFragment,
                    Bundle().apply { putSerializable("coord", weather.coord) }
                )
            }

            override fun onItemLongClick(weather: CurrentWeatherEntity) {
                viewModel.removeCity(weather)
            }
        }

        citiesAdapter = CitiesAdapter(onItemClickListener)
        binding?.citiesRecycler?.adapter = citiesAdapter
    }

    override fun setupListeners() {
        binding?.addButton?.setOnClickListener {
            context?.let { context ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Title")

                val input = EditText(context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)

                builder.setPositiveButton("OK") { dialog, which ->
                    viewModel.addCity(input.text.toString())
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }

                builder.show()
            }
        }
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