package com.bchmsl.mweatherapp.model


import com.google.gson.annotations.SerializedName

data class CitiesItem(
    @SerializedName("name")
    val name: String?,
)

class CitiesList : ArrayList<CitiesItem>()
