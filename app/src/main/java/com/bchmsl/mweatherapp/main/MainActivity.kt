package com.bchmsl.mweatherapp.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.bchmsl.mweatherapp.adapter.DailyAdapter
import com.bchmsl.mweatherapp.databinding.ActivityMainBinding
import com.bchmsl.mweatherapp.databinding.LayoutDialogBinding
import com.bchmsl.mweatherapp.model.CurrentData
import com.bchmsl.mweatherapp.model.DailyData
import com.bchmsl.mweatherapp.setImage
import kotlinx.coroutines.launch

private const val USER_PREFERENCES_NAME = "user_city"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

class MainActivity : AppCompatActivity() {
    companion object {
        val CITY = stringPreferencesKey("city")

    }
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val dailyAdapter by lazy { DailyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        viewModel.readJson()
        getData()
        listeners()
    }

    private fun listeners() {
        binding.tvCity.setOnClickListener {
            makeDialog()
        }
    }

    private fun getData(city: String? = null) {
        lifecycleScope.launch {
            if (city == null) {
                this@MainActivity.dataStore.data.collect {
                    val dataStoreValue = it[CITY]
                    if (dataStoreValue == null){
                        getDataWithCity("Tbilisi")
                    }else{
                        getDataWithCity(it[CITY]!!)
                    }
                }
            } else {
                getDataWithCity(city)
            }
            this@MainActivity.dataStore.edit {
                it[CITY] = binding.tvCity.text.toString()
            }
        }
    }

    private suspend fun getDataWithCity(city: String){
        viewModel.apply {
            getDailyWeather(city)
            getCurrentForecast(city)
            Log.wtf("TAG", city)
            viewModel.dailyData.observe(this@MainActivity) {
                setupDailyForecast(it)
            }
            viewModel.currentForecast.observe(this@MainActivity) {
                setupCurrentForecast(it)
            }
        }
    }

    private fun setupDailyForecast(dailyData: DailyData) {

        binding.rvForecast.apply {
            adapter = dailyAdapter
        }
        dailyAdapter.submitList(viewModel.getDailyList(dailyData))
    }

    @SuppressLint("SetTextI18n")
    private fun setupCurrentForecast(currentData: CurrentData) {
        binding.apply {
            tvCity.text = currentData.name
            tvTempCurrent.text = currentData.main?.temp?.toInt().toString() + "°C"
            tvTempMinimum.text = currentData.main?.tempMin?.toInt().toString() + "°C"
            tvTempMaximum.text = currentData.main?.tempMax?.toInt().toString() + "°C"
            currentData.weather?.get(0)?.icon?.let { ivIcon.setImage(it) }
        }
    }

    private fun makeDialog() {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getCitiesList())
        val binding = LayoutDialogBinding.inflate(LayoutInflater.from(this))
        binding.actvCities.setAdapter(arrayAdapter)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Choose city").setPositiveButton(
            "Select"
        ) { _, _ ->
            if (binding.actvCities.text.toString() !in viewModel.getCitiesList()) {
                Toast.makeText(this, "Invalid City!", Toast.LENGTH_SHORT).show()
            } else {
                getData(binding.actvCities.text.toString())
            }
        }.setNegativeButton(
            "Cancel"
        ) { dialog, _ ->
            dialog.cancel()
        }.setView(binding.root)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}