package com.nshumskii.testweatherapp.data.local.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.model.common.Weather
import com.nshumskii.testweatherapp.data.model.current_weather.Clouds
import com.nshumskii.testweatherapp.data.model.current_weather.Main
import com.nshumskii.testweatherapp.data.model.current_weather.Wind
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class CurrentWeatherEntity(
    val base: String,
    @Embedded val clouds: Clouds,
    val cod: Int,
    @Embedded val coord: Coord,
    val dt: Long,
    @PrimaryKey val id: Int,
    @Embedded val main: Main,
    val name: String,
//    @Embedded val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded val wind: Wind
) : Parcelable