package com.bchmsl.mweatherapp.model
import com.google.gson.annotations.SerializedName

data class DailyData(
    @SerializedName("dt")
    val id: Int?,
    @SerializedName("list")
    val list: List<DailyTEMP>?,
) {
    data class DailyTEMP(
        @SerializedName("dt_txt")
        val dtTxt: String?,
        @SerializedName("main")
        val main: Main?,
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
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("main")
            val main: String?
        )
    }
}