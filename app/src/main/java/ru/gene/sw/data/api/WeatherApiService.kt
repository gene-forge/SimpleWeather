package ru.gene.sw.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.gene.sw.data.model.ForecastResponse

interface WeatherApiService {

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 1,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): ForecastResponse
}
