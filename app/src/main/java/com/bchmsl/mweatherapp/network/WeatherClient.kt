package com.bchmsl.mweatherapp.network

import com.bchmsl.mweatherapp.BuildConfig
import com.bchmsl.mweatherapp.model.CurrentData
import com.bchmsl.mweatherapp.model.DailyData
import com.bchmsl.mweatherapp.model.Units
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherClient {

    @GET("data/2.5/forecast")
    suspend fun getDailyData(
        @Query("q") city: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = Units.METRIC.parameter
    ): Response<DailyData>

    @GET("data/2.5/weather")
    suspend fun getCurrentData(
        @Query("q") city: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = Units.METRIC.parameter
    ): Response<CurrentData>

}

