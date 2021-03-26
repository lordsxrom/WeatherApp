package com.nshumskii.testweatherapp.data.local

import androidx.room.*
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM currentweatherentity")
    fun getAllCurrentWeather(): Flow<List<CurrentWeatherEntity>>

    @Query("SELECT * FROM currentweatherentity WHERE name = :cityName")
    fun getCurrentWeather(cityName: String): CurrentWeatherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currentWeatherEntities: List<CurrentWeatherEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherEntity: CurrentWeatherEntity)

    @Delete
    suspend fun delete(currentWeatherEntity: CurrentWeatherEntity)

}