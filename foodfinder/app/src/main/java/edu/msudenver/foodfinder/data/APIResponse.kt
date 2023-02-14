package com.example.example
/*

* CS3013 - Mobile App Dev. - Summer 2022

* Instructor: Thyago Mota

* Student(s): Colin Stallings

* Description: Food Finder App

*/

import com.google.gson.annotations.SerializedName


data class APIResponse (

  @SerializedName("html_attributions" ) var htmlAttributions : ArrayList<String>  = arrayListOf(),
  @SerializedName("next_page_token"   ) var nextPageToken    : String?            = null,
  @SerializedName("results"           ) var results          : ArrayList<Results> = arrayListOf()

)