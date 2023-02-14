package com.example.example
/*

* CS3013 - Mobile App Dev. - Summer 2022

* Instructor: Thyago Mota

* Student(s): Colin Stallings

* Description: Food Finder App

*/

import com.google.gson.annotations.SerializedName


data class Photos (

  @SerializedName("height"            ) var height           : Int?              = null,
  @SerializedName("html_attributions" ) var htmlAttributions : ArrayList<String> = arrayListOf(),
  @SerializedName("photo_reference"   ) var photoReference   : String?           = null,
  @SerializedName("width"             ) var width            : Int?              = null

)