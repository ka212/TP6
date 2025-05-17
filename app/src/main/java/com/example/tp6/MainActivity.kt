package com.example.tp6

import adapters.CityAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp6.databinding.ActivityMainBinding
import data.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), CityAdapter.OnCityClickListener {

    private lateinit var binding: ActivityMainBinding

    private val apiKey = "928719a363128b8797b45d73672bd981"


    private val cities = arrayOf(
        "Tunis", "Paris", "Londres", "Pekin", "Tokyo", "Ottawa",
        "Alger", "Moscou", "Berlin", "Madrid", "Montevideo", "Buenos Aires"
    )

    private lateinit var retrofit: Retrofit
    private lateinit var weatherApiService: WeatherApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerViewCities.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCities.adapter = CityAdapter(cities, this)

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApiService = retrofit.create(WeatherApiService::class.java)

    }


    private fun getWeatherData(city: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = weatherApiService.getCurrentWeather(city, apiKey)

                withContext(Dispatchers.Main) {
                    binding.textViewCountry.text = "Pays : ${response.sys.country}"
                    binding.textViewTemp.text = "Température : ${response.main.temp}°C"
                    binding.textViewHumidity.text = "Humidité : ${response.main.humidity}%"
                    binding.textViewWind.text = "Vent : ${response.wind.speed} m/s"
                    binding.textViewDescription.text = "Description : ${response.weather[0].description}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCityClick(city: String) {
        Toast.makeText(this, "Ville sélectionnée : $city", Toast.LENGTH_SHORT).show()
        getWeatherData(city)
    }


}
