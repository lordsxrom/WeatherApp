package com.nshumskii.testweatherapp.data.local

import androidx.room.*
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity

@Dao
interface ForecastDao {

    @Query("SELECT * FROM onecallentity WHERE lat = :lat AND lon = :lon")
    fun getOnecallForecast(lat: Double, lon: Double): OnecallEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(onecallEntity: OnecallEntity)

    @Delete
    suspend fun delete(onecallEntity: OnecallEntity)

}