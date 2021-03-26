package com.nshumskii.testweatherapp.data.remote

import com.nshumskii.testweatherapp.data.remote.pojo.CurrentWeatherResponse
import com.nshumskii.testweatherapp.data.remote.pojo.OnecallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(@Query("q") cityName: String): Response<CurrentWeatherResponse>

    @GET("/data/2.5/onecall")
    suspend fun getForecast(@Query("lat") lat: Double, @Query("lon") lon: Double): Response<OnecallResponse>

}