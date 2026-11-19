package com.example.weatherdatabinding

data class WeatherResponse(
    val name: String = "",
    val weather: List<WeatherCondition>? = null,
    val main: MainData? = null,
    val wind: WindData? = null,
    val clouds: CloudsData? = null,
    val cod: Int = 0
)

data class WeatherCondition(
    val id: Int = 0,
    val main: String = "",
    val description: String = "",
    val icon: String = ""
)

data class MainData(
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val pressure: Double = 0.0,
    val humidity: Int = 0
)

data class WindData(
    val speed: Double = 0.0,
    val deg: Float = 0f
)

data class CloudsData(
    val all: Int = 0
)

fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        cityName = name,
        temp = main?.temp ?: 0.0,
        description = weather?.firstOrNull()?.description ?: "",
        humidity = main?.humidity ?: 0,
        pressure = main?.pressure ?: 0.0,
        windSpeed = wind?.speed ?: 0.0,
        windDeg = wind?.deg ?: 0f,
        clouds = clouds?.all ?: 0,
        hasData = true,
        error = null
    )
}
