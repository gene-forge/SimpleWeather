package ru.gene.sw.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val locationName: String,
    val country: String,
    val tempC: Double,
    val feelsLikeC: Double,
    val windKph: Double,
    val humidity: Int,
    val conditionText: String,
    val updatedAt: String
)