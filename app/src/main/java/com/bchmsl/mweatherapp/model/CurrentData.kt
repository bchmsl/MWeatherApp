package com.bchmsl.mweatherapp.model

import com.google.gson.annotations.SerializedName

data class CurrentData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("weather")
    val weather: List<Weather?>?,
) {
    data class Main(
        @SerializedName("temp")
        val temp: Double?,
        @SerializedName("temp_max")
        val tempMax: Double?,
        @SerializedName("temp_min")
        val tempMin: Double?
    )
    data class Weather(
        @SerializedName("description")
        val description: String?,
        @SerializedName("icon")
        val icon: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("main")
        val main: String?
    )
}