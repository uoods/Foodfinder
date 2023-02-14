package edu.msudenver.foodfinder

/*

* CS3013 - Mobile App Dev. - Summer 2022

* Instructor: Thyago Mota

* Student(s): Colin Stallings

* Description: Food Finder App

*/


import android.database.sqlite.SQLiteDatabase
//import android.icu.number.NumberFormatter.with
//import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class DisplayLocationActivity : AppCompatActivity() {

    var imgMap: ImageView? = null
    lateinit var db: SQLiteDatabase

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/api/staticmap"
        const val ZOOM = 13
        const val SIZE = "320x480"
    }
    }

//implement the function
// fun retrieveLocation(id: Int): Location {
// val cursor = db.query(
// "locations",
// arrayOf<String>("id, name, address"),
// "rowid = ${id}",
// null,
// null,
// null,
// null
// )
// with (cursor) {
// cursor.moveToNext()
// val id = getInt(0)
// val name = getString(1)
// val address = getString(2)
//
// return Location(id, name, address)
// }
// }
//
// override fun onCreate(savedInstanceState: Bundle?) {
// super.onCreate(savedInstanceState)
// setContentView(R.layout.activity_display_location)
//
// val dbHelper = DBHelper(this)
// db = dbHelper.readableDatabase
// val id = intent.getIntExtra("id", 0)
// val location = retrieveLocation(id)
//
// //update the image view with the result of google maps
// imgMap = findViewById(R.id.foodView)
// val key = getString(R.string.GCP_MAPS_API_KEY)
// // val markers = "${location.latitude},${location.longitude}"
// // val urlString = BASE_URL + "?key=${key}&zoom=${ZOOM}&size=${SIZE}&markers=${markers}"
// //        val urlString = "${BASE_URL}?key=${GCP_MAPS_API_KEY}&zoom=${ZOOM}&size=${SIZE}&markers=${location.latitude},${location.longitude}"
// //        imgMap = findViewById(R.id.img_map)
// // var uri = Uri.parse(urlString)
// //        Picasso.with(this).load(urlString).into(imgMap)
// // Picasso.get().load(uri).into(imgMap)
// }
// }