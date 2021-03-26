package com.nshumskii.testweatherapp.data.adapters

import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.remote.pojo.CurrentWeatherResponse

fun CurrentWeatherResponse.toEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        base = base,
        clouds = clouds,
        cod = cod,
        coord = coord,
        dt = dt,
        id = id,
        main = main,
        name = name,
//        sys = sys,
        timezone = timezone,
        visibility = visibility,
        weather = weather,
        wind = wind
    )
}