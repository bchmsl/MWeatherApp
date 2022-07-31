package com.bchmsl.mweatherapp

import android.widget.ImageView
import com.bumptech.glide.Glide


fun ImageView.setImage(icon: String){
    Glide.with(this).load("https://openweathermap.org/img/wn/$icon@4x.png").into(this)
}