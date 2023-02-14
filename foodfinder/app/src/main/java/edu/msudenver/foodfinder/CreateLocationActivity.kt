package edu.msudenver.foodfinder



import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.example.APIResponse
import com.example.example.Results
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.msudenver.foodfinder.api.PlacesAPI
import edu.msudenver.foodfinder.api.RetrofiTHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

class CreateLocationActivity : AppCompatActivity() {

    private var currentIndex: Int=0
    private var resultData: ArrayList<Results>? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var db: SQLiteDatabase
    var placeName : TextView? = null
    var yesBtn : Button? = null
    var noBtn : Button? = null
    var foodView : ImageView? =null
    var latLong = ""
    var photoBaseUrl =   "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
    val photoSubUrl = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_location)
        val dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase

        //creates back button with parentactivity manifest
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        yesBtn = findViewById(R.id.yesBtn)
        noBtn = findViewById(R.id.noBtn)
        foodView = findViewById(R.id.foodView)
        placeName = findViewById(R.id.placeName)



    }

    private fun loadNewData(result: Results) {
        if(result.photos[0].photoReference!!.isNotEmpty()) {
            val url = photoBaseUrl+result.photos[0].photoReference+photoSubUrl
            placeName?.text=result.name
            runOnUiThread(Runnable {
                Glide.with(applicationContext).load(url).into(foodView!!)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@CreateLocationActivity)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            latLong=it.result.latitude.toString()+","+it.result.longitude.toString()

            Log.e("@@@",latLong.toString())

            val placeAPI = RetrofiTHelper.getInstance().create(PlacesAPI::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val resultCall = placeAPI.getNearByPlaces(
                    latLong,
                    "restaurant",
                    "1500",
                    "AIzaSyBILrF0iPmswUjgAaGPwod_ufCLWeC5WTo"
                )
                resultCall.body().let {
                    resultData = it?.results
                    if(resultData!=null && resultData?.size!! > 0) {
                        resultData?.get(0).let {
                            loadNewData(it!!)
                        }
                    }else{
                        //Toast.makeText(this@CreateLocationActivity,"No Restaurant Found",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
     fun onFoodFinderOver(location: String) {
        try {
            val contentValue = ContentValues()
            contentValue.put("name", location)
            contentValue.put("address",location)
            db.insert("locations",null,contentValue)
            db.close()
            Toast.makeText(
                this,
                "Your location was recorded!",
                Toast.LENGTH_SHORT
            ).show()
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Exception when trying to record your location!",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}
