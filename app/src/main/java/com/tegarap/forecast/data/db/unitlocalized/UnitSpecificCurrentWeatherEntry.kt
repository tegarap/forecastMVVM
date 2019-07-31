package com.tegarap.forecast.data.db.unitlocalized

interface UnitSpecificCurrentWeatherEntry {
    val temperatures: Double
    val conditionText: String
    val conditionIconUrl: String
    val windSpeed: Double
    val windDirection: String
    val precipitationVolume: Double
    val feelsLikeTemperature: Double
    val visibilityDistance: Double
}