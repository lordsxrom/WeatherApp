package com.nshumskii.testweatherapp.data.repository

import com.nshumskii.testweatherapp.data.adapters.toEntity
import com.nshumskii.testweatherapp.data.local.ForecastDao
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.remote.WeatherRemoteDataSource
import com.nshumskii.testweatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: ForecastDao
) {

    suspend fun getOnecallForecast(coord: Coord): Flow<Result<OnecallEntity>> = flow {
        emit(Result.success(localDataSource.getOnecallForecasts(coord.lat, coord.lon)))
        emit(Result.loading())
        remoteDataSource.getForecast(coord).apply {
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

}