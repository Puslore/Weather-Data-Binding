package com.example.weatherdatabinding

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherDataTest {

    private val jsonSample = """
        {
          "coord": {"lon": 104.2964, "lat": 52.2978},
          "weather": [
            {
              "id": 600,
              "main": "Snow",
              "description": "небольшой снег",
              "icon": "13d"
            }
          ],
          "main": {
            "temp": -2.57,
            "feels_like": -6.59,
            "pressure": 1032.0,
            "humidity": 74
          },
          "wind": {
            "speed": 3.0,
            "deg": 250.0
          },
          "clouds": {
            "all": 20
          },
          "name": "Иркутск",
          "cod": 200
        }
    """.trimIndent()

    @Test
    fun testWeatherJsonParsing() {
        val response = Gson().fromJson(jsonSample, WeatherResponse::class.java)

        assertNotNull(response)
        assertEquals("Иркутск", response.name)
        assertEquals(74, response.main?.humidity)
        assertEquals(-2.57, response.main?.temp ?: 0.0, 0.01)
        assertEquals(250.0f, response.wind?.deg ?: 0f, 0.01f)
        assertEquals(20, response.clouds?.all)
    }

    @Test
    fun testWeatherResponseToWeatherData() {
        val response = Gson().fromJson(jsonSample, WeatherResponse::class.java)
        val weather = response.toWeatherData()

        assertEquals("Иркутск", weather.cityName)
        assertEquals(-2.57, weather.temp, 0.01)
        assertEquals("небольшой снег", weather.description)
        assertEquals(74, weather.humidity)
        assertEquals(1032.0, weather.pressure, 0.01)
        assertEquals(3.0, weather.windSpeed, 0.01)
        assertEquals(250.0f, weather.windDeg, 0.01f)
        assertEquals(20, weather.clouds)
        assertEquals(0.20f, weather.cloudAlpha, 0.01f)
        assertTrue(weather.hasData)
        assertNull(weather.error)
    }

    @Test
    fun testWeatherDataModel() {
        val weather = WeatherData(
            cityName = "Иркутск",
            temp = -2.5,
            description = "небольшой снег",
            windDeg = 250f,
            clouds = 20,
            hasData = true
        )

        assertEquals("Иркутск", weather.cityName)
        assertEquals(-2.5, weather.temp, 0.01)
        assertEquals(250f, weather.windDeg, 0.01f)
        assertEquals(20, weather.clouds)
        assertTrue(weather.hasData)
        assertNull(weather.error)
    }

    @Test
    fun testCelsiusFormatting() {
        val weather = WeatherData(temp = -2.5, hasData = true)

        assertEquals("Температура: -2.5 °C", weather.formatTemperature(false))
    }

    @Test
    fun testFahrenheitFormatting() {
        val freezing = WeatherData(temp = 0.0, hasData = true)
        val boiling = WeatherData(temp = 100.0, hasData = true)
        val equal = WeatherData(temp = -40.0, hasData = true)

        assertEquals("Температура: 32.0 °F", freezing.formatTemperature(true))
        assertEquals("Температура: 212.0 °F", boiling.formatTemperature(true))
        assertEquals("Температура: -40.0 °F", equal.formatTemperature(true))
    }
}
