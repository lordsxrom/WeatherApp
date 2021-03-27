package com.nshumskii.testweatherapp.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nshumskii.testweatherapp.data.model.onecall.*

@Entity
data class OnecallEntity(
    @PrimaryKey val id: String,
    val alerts: List<Alert>?,
    @Embedded val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val timezone: String,
    val timezone_offset: Int
)