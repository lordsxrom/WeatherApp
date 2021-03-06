package com.nshumskii.testweatherapp.data.repository

import com.nshumskii.testweatherapp.data.adapters.toEntity
import com.nshumskii.testweatherapp.data.local.CurrentWeatherDao
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.remote.OpenWeatherDataSource
import com.nshumskii.testweatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val remoteDataSource: OpenWeatherDataSource,
    private val localDataSource: CurrentWeatherDao
) {

    suspend fun getCurrentWeather(cityName: String): Flow<Result<CurrentWeatherEntity>> = flow {
        emit(Result.success(localDataSource.get(cityName)))
        emit(Result.loading())
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

    suspend fun getCurrentWeathers(): Flow<Result<List<CurrentWeatherEntity>>> = flow {
        emit(Result.loading())
        emitAll(localDataSource.getAll().map { entity ->
            Result.success(entity)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun findCurrentWeather(cityName: String): Flow<Result<Nothing?>> = flow {
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

    suspend fun deleteCurrentWeather(entity: CurrentWeatherEntity) =
        localDataSource.delete(entity)

    suspend fun undoDeleteCurrentWeather(entity: CurrentWeatherEntity) =
        localDataSource.insert(entity)

}