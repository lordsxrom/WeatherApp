package com.nshumskii.testweatherapp.ui.cities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.repository.CurrentWeatherRepository
import com.nshumskii.testweatherapp.utils.Event
import com.nshumskii.testweatherapp.utils.Result
import com.nshumskii.testweatherapp.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) : BaseViewModel() {

    private val _currentWeather: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeatherEntity> get() = _currentWeather

    private val _citiesWeathers: MutableLiveData<List<CurrentWeatherEntity>> = MutableLiveData()
    val citiesWeathers: LiveData<List<CurrentWeatherEntity>> get() = _citiesWeathers

    private val weathersEventChannel = Channel<WeathersEvent>()
    val weathersEvent = weathersEventChannel.receiveAsFlow()

    init {
        getCurrentWeathers()
    }

    fun getCurrentWeather(cityName: String) {
        viewModelScope.launch {
            currentWeatherRepository.getCurrentWeather(cityName).collect { result ->
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

    fun getCurrentWeathers() {
        viewModelScope.launch {
            currentWeatherRepository.getCurrentWeathers().collect { result ->
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

    fun findCurrentWeather(cityName: String) {
        Log.d("CitiesViewModel", "addCity: $cityName")
        viewModelScope.launch {
            currentWeatherRepository.findCurrentWeather(cityName).collect { result ->
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

    fun onDelete(entity: CurrentWeatherEntity) = viewModelScope.launch {
        currentWeatherRepository.deleteCurrentWeather(entity)
        weathersEventChannel.send(WeathersEvent.ShowUndoDeleteWeatherMessage(entity))
    }

    fun onUndoDelete(entity: CurrentWeatherEntity) = viewModelScope.launch {
        currentWeatherRepository.undoDeleteCurrentWeather(entity)
    }

    sealed class WeathersEvent {
        data class ShowUndoDeleteWeatherMessage(val weather: CurrentWeatherEntity) : WeathersEvent()
    }

}