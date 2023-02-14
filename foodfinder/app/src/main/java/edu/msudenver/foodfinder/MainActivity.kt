package edu.msudenver.foodfinder

/*

* CS3013 - Mobile App Dev. - Summer 2022

* Instructor: Thyago Mota

* Student(s): Colin Stallings

* Description: Food Finder App

*/


import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var dbHelper: DBHelper

    // TODO #1: implement the LocationHolder class
    private inner class LocationHolder(view: View): RecyclerView.ViewHolder(view) {
        var txtID: TextView = view.findViewById(R.id.txtID)
        var txtName: TextView = view.findViewById(R.id.txtName)
        var txtAddress: TextView = view.findViewById(R.id.txtAddress)

    }

    // TODO #2: implement the LocationAdapter class
    private inner class LocationAdapter(var locations: List<Location>, var onClickListener: View.OnClickListener, var onLongClickListener: View.OnLongClickListener): RecyclerView.Adapter<LocationHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.location_list, parent, false)
            view.setOnClickListener(onClickListener)
            view.setOnLongClickListener(onLongClickListener)
            return LocationHolder(view)
        }

        override fun onBindViewHolder(holder: LocationHolder, position: Int) {
            val location = locations[position]
            holder.txtID.text = location.id.toString()
            holder.txtName.text = location.name
            holder.txtAddress.text = location.address


        }

        override fun getItemCount(): Int {
            return locations.size
        }
    }

    // TODO #3: implement the function
    fun populateRecyclerView() {
        val db = dbHelper.readableDatabase
        val locations = mutableListOf<Location>()
        val cursor = db.query(
            "locations",
            arrayOf<String>("rowid, name, address"),
            null,
            null,
            null,
            null,
            null
        )
        with (cursor) {
            while (moveToNext()) {
                val id   = getInt(0)
                val name = getString(1)
                val address = getString(2)
                val location = Location(id,name, address)
                locations.add(location)
            }
        }
        recyclerView.adapter = LocationAdapter(locations, this, this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

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

        // TODO #4: create and populate the recycler view
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateRecyclerView()

        // TODO #5: transition to CreateLocationActivity
        val fabCreate: FloatingActionButton = findViewById(R.id.fabCreate)
        fabCreate.setOnClickListener {
            val intent = Intent(this, CreateLocationActivity::class.java)
            startActivity(intent)
        }
    }

    // this method is called when CreateLocationActivity finishes
    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }

    // TODO #6: transition to DisplayLocationActivity (passing the location's "id")
    override fun onClick(view: View?) {
        if (view != null) {
            val txtID: TextView = view.findViewById(R.id.txtID)
            val id = txtID.text.toString().toInt()
            val intent = Intent(this, DisplayLocationActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    // TODO #7: delete the location identified by "id"
    override fun onLongClick(view: View?): Boolean {
        class MyDialogInterfaceListener(val id: Int): DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    try {
                        val db = dbHelper.writableDatabase
                        db.execSQL("""
                            DELETE FROM locations
                            WHERE rowid = ${id}
                             """)
                        populateRecyclerView()
                    } catch (ex: Exception) {

                    }
                }
            }
        }

        if (view != null) {
            val id = view.findViewById<TextView>(R.id.txtID).text.toString().toInt()
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Are you sure you want to delete the location?")
            alertDialogBuilder.setPositiveButton("Yes", MyDialogInterfaceListener(id))
            alertDialogBuilder.setNegativeButton("No", MyDialogInterfaceListener(id))
            alertDialogBuilder.show()
            return true
        }
        return false    }

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
}