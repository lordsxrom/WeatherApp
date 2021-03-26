package com.nshumskii.testweatherapp.ui.cities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.repository.WeatherRepository
import com.nshumskii.testweatherapp.utils.Event
import com.nshumskii.testweatherapp.utils.Result
import com.nshumskii.testweatherapp.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : BaseViewModel() {

    private val _currentWeather: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeatherEntity> get() = _currentWeather

    private val _citiesWeathers: MutableLiveData<List<CurrentWeatherEntity>> = MutableLiveData()
    val citiesWeathers: LiveData<List<CurrentWeatherEntity>> get() = _citiesWeathers

    init {
        getCitiesList()
    }

    fun getCurrentWeather(cityName: String) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(cityName).collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                        result.data?.let { _currentWeather.value = it }
                    }
                    Result.Status.ERROR -> {
                        progress.value = Event(false)
                        error.value = Event(result.message ?: "Error")
                    }
                    Result.Status.LOADING -> {
                        progress.value = Event(true)
                    }
                }
            }
        }
    }

    fun getCitiesList() {
        viewModelScope.launch {
            weatherRepository.getCitiesList().collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                        result.data?.let { _citiesWeathers.value = it }
                    }
                    Result.Status.ERROR -> {
                        progress.value = Event(false)
                        error.value = Event(result.message ?: "Error")
                    }
                    Result.Status.LOADING -> {
                        progress.value = Event(true)
                    }
                }
            }
        }
    }

    fun addCity(cityName: String) {
        Log.d("CitiesViewModel", "addCity: $cityName")
        viewModelScope.launch {
            weatherRepository.addCity(cityName).collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                    }
                    Result.Status.ERROR -> {
                        progress.value = Event(false)
                        error.value = Event(result.message ?: "Error")
                    }
                    Result.Status.LOADING -> {
                        progress.value = Event(true)
                    }
                }
            }
        }
    }

    fun removeCity(entity: CurrentWeatherEntity) {
        viewModelScope.launch {
            weatherRepository.removeCity(entity).collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                    }
                    Result.Status.LOADING -> {
                        progress.value = Event(true)
                    }
                }
            }
        }
    }

}