package data

data class WeatherResponse(
    val sys: Sys,
    val summary: String,
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)

data class Main(val temp: Double, val humidity: Int)
data class Wind(val speed: Double)
data class Weather(val description: String)
data class Sys(val country : String)