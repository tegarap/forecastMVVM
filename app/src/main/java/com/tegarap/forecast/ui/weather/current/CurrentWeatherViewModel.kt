package com.tegarap.forecast.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.tegarap.forecast.data.repository.ForecastRepository
import com.tegarap.forecast.internal.UnitSystem

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazy {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
