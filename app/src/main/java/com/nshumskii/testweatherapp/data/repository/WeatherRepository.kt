package com.nshumskii.testweatherapp.data.repository

import com.nshumskii.testweatherapp.data.adapters.toEntity
import com.nshumskii.testweatherapp.data.local.WeatherDao
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.remote.WeatherRemoteDataSource
import com.nshumskii.testweatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherDao
) {

    suspend fun getCurrentWeather(cityName: String): Flow<Result<CurrentWeatherEntity>> = flow {
        emit(Result.loading())
        emit(Result.success(localDataSource.getCurrentWeather(cityName)))

        remoteDataSource.getCurrentWeather(cityName).apply {
            if (status == Result.Status.SUCCESS) {
                data?.let { data ->
                    val entity = data.toEntity()
                    localDataSource.insert(entity)
                    emit(Result.success(entity))
                }
            } else if (status == Result.Status.ERROR) {
                emit(Result.error(message ?: "Undefined error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getCitiesList(): Flow<Result<List<CurrentWeatherEntity>>> = flow {
        emit(Result.loading())
        emitAll(localDataSource.getAllCurrentWeather().map { entity ->
            Result.success(entity)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun addCity(cityName: String): Flow<Result<Nothing?>> = flow {
        emit(Result.loading())
        val responseStatus = remoteDataSource.getCurrentWeather(cityName)
        if (responseStatus.status == Result.Status.SUCCESS) {
            responseStatus.data?.let { data ->
                localDataSource.insert(data.toEntity())
            }
        } else if (responseStatus.status == Result.Status.ERROR) {
            emit(Result.error("Undefined error"))
        }
    }

    suspend fun removeCity(entity: CurrentWeatherEntity): Flow<Result<Nothing?>> = flow {
        emit(Result.loading())
        localDataSource.delete(entity)
        emit(Result.success(null))
    }

}