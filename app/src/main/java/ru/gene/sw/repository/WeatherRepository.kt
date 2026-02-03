package ru.gene.sw.repository

import kotlinx.coroutines.flow.Flow
import ru.gene.sw.Config
import ru.gene.sw.data.api.IpApiService
import ru.gene.sw.data.api.WeatherApiService
import ru.gene.sw.data.db.dao.WeatherDao
import ru.gene.sw.data.db.entity.WeatherEntity
import ru.gene.sw.data.model.ForecastResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApiService,
    private val dao: WeatherDao,
    private val ipApi: IpApiService
) {

    suspend fun getLocationByIp(lang: String): String {
        return try {
            val loc = ipApi.getLocation(lang)
            "${loc.lat},${loc.lon}"
        } catch (e: Exception) {
            Config.DEFAULT_LOCATION
        }
    }

    suspend fun getForecast(apiKey: String, location: String): ForecastResponse {
        val forecast = api.getForecast(apiKey, location, Config.FORECAST_DAYS)
        dao.insertWeather(
            WeatherEntity(
                locationName = forecast.location.name,
                country = forecast.location.country,
                tempC = forecast.current.temp_c,
                feelsLikeC = forecast.current.feelslike_c,
                windKph = forecast.current.wind_kph,
                humidity = forecast.current.humidity,
                conditionText = forecast.current.condition.text,
                updatedAt = forecast.location.localtime
            )
        )
        return forecast
    }



    fun getCachedWeather(): Flow<WeatherEntity?> = dao.getLatestWeather()
}