package com.tegarap.forecast.data.repository

import androidx.lifecycle.LiveData
import com.tegarap.forecast.data.db.CurrentWeatherDao
import com.tegarap.forecast.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.tegarap.forecast.data.network.WeatherNetworkDataSource
import com.tegarap.forecast.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import java.util.*

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
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData() {
        if(isFetchedCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            "Jakarta",
            Locale.getDefault().language
        )
    }

    private fun isFetchedCurrentNeeded(lastTimeFeched: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastTimeFeched.isBefore(thirtyMinutesAgo)
    }
}