package com.nshumskii.testweatherapp.data.remote.pojo

import com.nshumskii.testweatherapp.data.model.onecall.*

data class OnecallResponse(
    val alerts: List<Alert>,
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val timezone: String,
    val timezone_offset: Int
)