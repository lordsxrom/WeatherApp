package com.nshumskii.testweatherapp.data.model.current_weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Clouds(
    val all: Int
): Parcelable