package com.nshumskii.testweatherapp.data.adapters

import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import com.nshumskii.testweatherapp.data.remote.pojo.OnecallResponse

fun OnecallResponse.toEntity(): OnecallEntity {
    return OnecallEntity(
        id = "$lat$lon",
        alerts = alerts,
        current = current,
        daily = daily,
        hourly = hourly,
        lat = lat,
        lon = lon,
        minutely = minutely,
        timezone = timezone,
        timezone_offset = timezone_offset
    )
}