package com.nshumskii.testweatherapp.data.remote.pojo

import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.model.common.Weather
import com.nshumskii.testweatherapp.data.model.current_weather.Clouds
import com.nshumskii.testweatherapp.data.model.current_weather.Main
import com.nshumskii.testweatherapp.data.model.current_weather.Sys
import com.nshumskii.testweatherapp.data.model.current_weather.Wind

data class CurrentWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Long,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

