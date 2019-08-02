package com.tegarap.forecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tegarap.forecast.data.db.entity.CURRENT_WEATHER_ID
import com.tegarap.forecast.data.db.entity.CurrentWeatherEntry
import com.tegarap.forecast.data.db.unitlocalized.ImperialCurrentWeatherEntry
import com.tegarap.forecast.data.db.unitlocalized.MetricCurrentWeatherEntry

@Dao
@Entity
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeatherEntry>

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}