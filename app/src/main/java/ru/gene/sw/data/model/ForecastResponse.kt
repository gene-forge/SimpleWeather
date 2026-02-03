package ru.gene.sw.data.model

data class ForecastResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime: String
)

data class Current(
    val temp_c: Double,
    val feelslike_c: Double,
    val wind_kph: Double,
    val humidity: Int,
    val pressure_mb: Double,
    val cloud: Int,
    val vis_km: Double,
    val uv: Double,
    val gust_kph: Double,
    val condition: Condition
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double,
    val daily_chance_of_rain: Int,
    val daily_chance_of_snow: Int,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

data class Hour(
    val time: String,
    val temp_c: Double,
    val feelslike_c: Double,
    val wind_kph: Double,
    val humidity: Int,
    val pressure_mb: Double,
    val chance_of_rain: Int,
    val chance_of_snow: Int,
    val condition: Condition
)

data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moon_phase: String
)
