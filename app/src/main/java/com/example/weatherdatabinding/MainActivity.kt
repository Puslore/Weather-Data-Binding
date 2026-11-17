package com.example.weatherdatabinding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherdatabinding.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = WeatherData()

        // Слушатель выбора города в выпадающем меню
        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
                loadWeatherAsync(selectedCity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    suspend fun loadWeather(city: String) {
        val apiKey = getString(R.string.weather_api_key)
        val encodedCity = URLEncoder.encode(city.trim(), "UTF-8")
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$encodedCity&appid=$apiKey&units=metric&lang=ru"

        try {
            val stream = URL(weatherURL).getContent() as InputStream
            val scanner = Scanner(stream)
            val data = if (scanner.hasNextLine()) scanner.nextLine() else ""
            scanner.close()

            if (data.isEmpty()) {
                throw Exception("Пустой ответ от сервера")
            }

            Log.d("mytag", data)

            val response = Gson().fromJson(data, WeatherResponse::class.java)

            val newWeatherData = WeatherData(
                cityName = response.name,
                temp = response.main?.temp ?: 0.0,
                description = response.weather?.firstOrNull()?.description ?: "",
                humidity = response.main?.humidity ?: 0,
                pressure = response.main?.pressure ?: 0.0,
                windSpeed = response.wind?.speed ?: 0.0,
                windDeg = response.wind?.deg ?: 0f,
                clouds = response.clouds?.all ?: 0,
                hasData = true,
                error = null
            )

            withContext(Dispatchers.Main) {
                binding.weather = newWeatherData
            }
        } catch (e: Exception) {
            Log.e("mytag", "Ошибка загрузки погоды", e)
            withContext(Dispatchers.Main) {
                binding.weather = WeatherData(
                    hasData = false,
                    error = "Ошибка: ${e.localizedMessage ?: "не удалось загрузить данные"}"
                )
            }
        }
    }

    public fun onClick(v: View) {
        val city = binding.spinnerCities.selectedItem?.toString() ?: "Иркутск"
        loadWeatherAsync(city)
    }

    private fun loadWeatherAsync(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            loadWeather(city)
        }
    }
}
