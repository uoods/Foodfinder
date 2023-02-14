package edu.msudenver.foodfinder.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofiTHelper {
    val baseURL = "https://maps.googleapis.com/maps/api/"

    fun getInstance():Retrofit{
        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit
    }
}