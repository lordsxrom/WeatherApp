package com.nshumskii.testweatherapp.data.local

import androidx.room.*
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Query("SELECT * FROM onecallentity")
    fun getOnecallForecasts(): List<OnecallEntity>

    @Query("SELECT * FROM onecallentity WHERE lat = :lat AND lon = :lon")
    fun getOnecallForecasts(lat: Double, lon: Double): OnecallEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(onecallEntities: List<OnecallEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(onecallEntity: OnecallEntity)

    @Delete
    suspend fun delete(onecallEntity: OnecallEntity)

}