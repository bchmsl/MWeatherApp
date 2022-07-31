package com.bchmsl.mweatherapp.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bchmsl.mweatherapp.model.*
import com.bchmsl.mweatherapp.network.RetrofitService
import com.google.gson.GsonBuilder
import java.io.IOException

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    companion object {
        val api = RetrofitService.getForecast()
        var cities = mutableListOf<CitiesItem?>()
    }

    private var _dailyData = MutableLiveData<DailyData>()
    val dailyData: LiveData<DailyData> get() = _dailyData

    private var _currentForecast = MutableLiveData<CurrentData>()
    val currentForecast: LiveData<CurrentData> get() = _currentForecast


    suspend fun getDailyWeather(city: String) {
        val response = api.getDailyData(city = city)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                _dailyData.postValue(body!!)
                Log.wtf("RESPONSE.BODY", body.toString())
            } else {
                Log.wtf("TAG", response.message())
            }
        }
    }

    suspend fun getCurrentForecast(city: String) {
        val response = api.getCurrentData(city = city)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                _currentForecast.postValue(body!!)
            } else {
                Log.wtf("TAG", response.message())
            }
        }
    }

    fun getDailyList(dailyData: DailyData): List<DailyData.DailyTEMP> {
        val dailyDataMap = mutableMapOf<String, DailyData.DailyTEMP>()
        dailyData.list?.forEach {
            val dtTxt = it.dtTxt?.split(" ")?.get(0) ?: ""
            dailyDataMap[dtTxt] = it
        }
        val result = dailyDataMap.values
        return result.toList()
    }
    fun getCitiesList():List<String>{
        val result = mutableListOf<String>()
        cities.forEach {
            it?.name?.let { it1 -> result.add(it1) }
        }
        return result
    }

    fun readJson() {
        try {
            val inputStream = app.assets.open("city.list.json")
            val gson = GsonBuilder().create().fromJson(
                inputStream.bufferedReader().use { it.readText() }, CitiesList::class.java)
            cities.addAll(gson.toList())
        } catch (e: IOException) {
            Log.wtf("TAG", e.toString())
        }
    }
}