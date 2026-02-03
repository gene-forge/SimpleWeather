package ru.gene.sw.data.db.entity

data class IpLocation(
    val status: String,
    val country: String,
    val countryCode: String,
    val region: String,
    val regionName: String,
    val city: String,
    val lat: Double,
    val lon: Double
)