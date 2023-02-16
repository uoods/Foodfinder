package edu.msudenver.foodfinder.api

import android.telecom.Call
import com.example.example.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?
// location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&
// key=

interface PlacesAPI {
    @GET("place/nearbysearch/json")
    suspend fun getNearByPlaces(@Query("location") location:String,@Query("type") type:String,@Query("radius") radius:String,@Query("key") key:String):Response<APIResponse>
}
