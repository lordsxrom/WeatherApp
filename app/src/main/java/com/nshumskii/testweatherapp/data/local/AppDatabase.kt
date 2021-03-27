package com.nshumskii.testweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity

@Database(entities = [CurrentWeatherEntity::class, OnecallEntity::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): CurrentWeatherDao

    abstract fun forecastDao(): OnecallDao

}