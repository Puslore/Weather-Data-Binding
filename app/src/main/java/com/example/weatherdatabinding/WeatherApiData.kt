package com.example.weatherdatabinding

data class WeatherResponse(
    val name: String,
    val weather: List<WeatherCondition>?,
    val main: MainData?,
    val wind: WindData?,
    val clouds: CloudsData?,
    val cod: Int
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainData(
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Int
)

data class WindData(
    val speed: Double,
    val deg: Float
)

data class CloudsData(
    val all: Int
)
