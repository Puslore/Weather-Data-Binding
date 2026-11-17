package com.example.weatherdatabinding

data class WeatherData(
    val cityName: String = "",
    val temp: Double = 0.0,
    val description: String = "",
    val humidity: Int = 0,
    val pressure: Double = 0.0,
    val windSpeed: Double = 0.0,
    val windDeg: Float = 0f,
    val clouds: Int = 0,
    val hasData: Boolean = false,
    val error: String? = null
) {
    val cloudAlpha: Float
        get() = clouds / 100f
}
