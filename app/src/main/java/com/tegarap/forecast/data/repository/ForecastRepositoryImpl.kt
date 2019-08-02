package com.tegarap.forecast.data.repository

import androidx.lifecycle.LiveData
import com.tegarap.forecast.data.db.CurrentWeatherDao
import com.tegarap.forecast.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.tegarap.forecast.data.network.WeatherNetworkDataSource
import com.tegarap.forecast.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }
}