package com.nshumskii.testweatherapp.data.remote

import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.remote.pojo.CurrentWeatherResponse
import com.nshumskii.testweatherapp.data.remote.pojo.OnecallResponse
import com.nshumskii.testweatherapp.utils.Result
import com.nshumskii.testweatherapp.utils.base.BaseDataSource
import javax.inject.Inject

class OpenWeatherDataSource @Inject constructor(
    private val openWeatherService: OpenWeatherService
) : BaseDataSource() {

    suspend fun getCurrentWeather(cityName: String): Result<CurrentWeatherResponse> =
        getResult { openWeatherService.getCurrentWeather(cityName) }

    suspend fun getOnecall(coord: Coord): Result<OnecallResponse> =
        getResult { openWeatherService.getOnecall(coord.lat, coord.lon) }

}