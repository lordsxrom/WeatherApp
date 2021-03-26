package com.nshumskii.testweatherapp.di

import android.content.Context
import androidx.room.Room
import com.nshumskii.testweatherapp.data.local.AppDatabase
import com.nshumskii.testweatherapp.data.local.ForecastDao
import com.nshumskii.testweatherapp.data.local.WeatherDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(appDatabase: AppDatabase): WeatherDao {
        return appDatabase.weatherDao()
    }

    @Provides
    @Singleton
    fun provideForecastDao(appDatabase: AppDatabase): ForecastDao {
        return appDatabase.forecastDao()
    }

}