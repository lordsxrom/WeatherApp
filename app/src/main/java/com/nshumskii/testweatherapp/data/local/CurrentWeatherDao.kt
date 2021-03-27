package com.nshumskii.testweatherapp.data.local

import androidx.room.*
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM currentweatherentity")
    fun getAll(): Flow<List<CurrentWeatherEntity>>

    @Query("SELECT * FROM currentweatherentity WHERE name = :cityName")
    suspend fun get(cityName: String): CurrentWeatherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherEntity: CurrentWeatherEntity)

    @Delete
    suspend fun delete(currentWeatherEntity: CurrentWeatherEntity)

}