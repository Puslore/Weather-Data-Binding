package com.example.weatherdatabinding

import java.util.Locale

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

    fun formatTemperature(useFahrenheit: Boolean): String {
        val value = if (useFahrenheit) temp * 9.0 / 5.0 + 32.0 else temp
        val unit = if (useFahrenheit) "°F" else "°C"
        return String.format(Locale.US, "Температура: %.1f %s", value, unit)
    }
}
