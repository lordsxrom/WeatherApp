package com.nshumskii.testweatherapp.data.repository

import com.nshumskii.testweatherapp.data.adapters.toEntity
import com.nshumskii.testweatherapp.data.local.OnecallDao
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.remote.OpenWeatherDataSource
import com.nshumskii.testweatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OnecallRepository @Inject constructor(
    private val remoteDataSource: OpenWeatherDataSource,
    private val localDataSource: OnecallDao
) {

    suspend fun getOnecall(coord: Coord): Flow<Result<OnecallEntity>> = flow {
        emit(Result.success(localDataSource.get(coord.lat, coord.lon)))
        emit(Result.loading())
        remoteDataSource.getOnecall(coord).apply {
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