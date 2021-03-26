package com.nshumskii.testweatherapp.di

import com.nshumskii.testweatherapp.AppConfig
import com.nshumskii.testweatherapp.data.remote.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkhttpInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apikey", AppConfig.key)
                .addQueryParameter("units", AppConfig.units)
                .build()
            val requestBuilder = original.newBuilder()
                .url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConfig.url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherService(retrofit: Retrofit): OpenWeatherService {
        return retrofit.create(OpenWeatherService::class.java)
    }

}