package com.nshumskii.testweatherapp.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.remote.pojo.OnecallResponse
import com.nshumskii.testweatherapp.data.repository.ForecastRepository
import com.nshumskii.testweatherapp.utils.Event
import com.nshumskii.testweatherapp.utils.Result
import com.nshumskii.testweatherapp.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository
) : BaseViewModel() {

    private val _forecasts: MutableLiveData<OnecallEntity> = MutableLiveData()
    val forecasts: LiveData<OnecallEntity> get() = _forecasts

    fun getForecast(coord: Coord) {
        viewModelScope.launch {
            forecastRepository.getOnecallForecast(coord).collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                        result.data?.let { _forecasts.value = it }
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

}