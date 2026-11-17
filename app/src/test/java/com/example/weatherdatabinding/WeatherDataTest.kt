package com.example.weatherdatabinding

import com.google.gson.Gson
import org.junit.Assert.*
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
        val gson = Gson()
        val response = gson.fromJson(jsonSample, WeatherResponse::class.java)

        assertNotNull(response)
        assertEquals("Иркутск", response.name)
        assertEquals(74, response.main?.humidity)
        assertEquals(-2.57, response.main?.temp ?: 0.0, 0.01)
        assertEquals(250.0f, response.wind?.deg ?: 0f, 0.01f)
        assertEquals(20, response.clouds?.all)
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
}
