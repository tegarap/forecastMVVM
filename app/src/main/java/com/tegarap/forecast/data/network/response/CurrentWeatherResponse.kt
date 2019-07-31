package com.tegarap.forecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.tegarap.forecast.data.db.entity.CurrentWeatherEntry
import com.tegarap.forecast.data.db.entity.Location


data class CurrentWeatherResponse(
    val location: Location,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)