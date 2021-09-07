package com.example.atlas


import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.*
import android.graphics.Color
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.widget.ButtonBarLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.math.sqrt


class AcceptedRequestesActivity : AppCompatActivity(), LocationListener {

    protected var locationManager: LocationManager? = null
    protected var context: Context? = null
    val This=this
    private var trace:String="Last Tracement: Nothing yet"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptedrequestes)

        var username=""
        var tel:String=""
        var description:String
        var shop=""
        var count:Int=0
        var username1=""
        var CountView:TextView
        var ButtonDescription:Button
        var ButtonDelete: Button
        var compratore:String=""
        var ButtonTrace: Button
        var onerow: TableRow
        var count1=0
        var lp: TableRow.LayoutParams
        var lb: TableRow.LayoutParams
        lb=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        lb.leftMargin=15
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        var prova:String=""
        val ref= FirebaseDatabase.getInstance()
        val uid= FirebaseAuth.getInstance().uid
        val table=findViewById<TableLayout>(R.id.AcceptedTable)
        shop=""
        var flag:Boolean=true
        ref.getReference("/ShopRequest").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    ref.getReference("ShopRequest/${it.key.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            snapshot.children.forEach {

                                if (it.key.toString().equals("Shopper")) {
                                    if (it.value.toString().equals(uid)) {
                                        ref.getReference("/users/$uid/username").addListenerForSingleValueEvent(object: ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                username1=snapshot.value.toString()
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                        ref.getReference("ShopRequest/${snapshot.key.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                snapshot.children.forEach{
                                                    if(it.key.toString().equals("uidp")){
                                                         compratore = it.value.toString()
                                                    }
                                                }
                                                count++
                                                CountView = TextView(This)
                                                CountView.setText(count.toString())
                                                CountView.setTextColor(Color.WHITE)
                                                CountView.setTextSize(20.0f)
                                                CountView.setPadding(10, 10, 10, 10)
                                                onerow = TableRow(This)
                                                lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)


                                                ButtonDelete = Button(This)
                                                ButtonDelete.setText("DELETE")
                                                ButtonDelete.setBackgroundResource(R.drawable.buttonblue)
                                                ButtonDelete.setTextColor(Color.WHITE)
                                                ButtonDelete.id=Integer.parseInt(snapshot.key.toString())
                                                ButtonDelete.setPadding(10,0,10,0)

                                                ButtonDelete.setOnClickListener(View.OnClickListener {b->ButtonDelete
                                                    AlertDialog.Builder(This)
                                                            .setTitle("Confirm Delete")
                                                            .setMessage("Are you sure that you want to delete this request?")

                                                            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                                                ref.getReference("/ShopRequest/${snapshot.key.toString()}/Shopper").setValue("")
                                                                //ref.getReference("/ShopRequest/$i/flag").setValue("false")
                                                                //ref.getReference("/ShopRequest/${snapshot.key.toString()}/deleteflag").setValue("true")
                                                                ref.getReference("/ShopRequest/${snapshot.key.toString()}/tracement").setValue("Last Tracement: Nothing yet")
                                                                ref.getReference("/Ntf/cont1").addListenerForSingleValueEvent(object:ValueEventListener{
                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                        Log.d("CONTATORE", snapshot.value.toString())
                                                                        count1=Integer.parseInt(snapshot.value.toString())
                                                                        count1++
                                                                        ref.getReference("/ShopRequest/${b.id}").addListenerForSingleValueEvent(object :ValueEventListener{
                                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                                snapshot.children.forEach{
                                                                                    if(it.key.toString().equals("shopps")){
                                                                                        shop=it.value.toString()
                                                                                    }
                                                                                }
                                                                                ref.getReference("/Ntf/$compratore/$count1").setValue("Your request: ("+shop+") has been canceled by "+username1)
                                                                                ref.getReference("/Ntf/cont1").setValue(count1)
                                                                                var intent = Intent(This, AcceptedRequestesActivity::class.java)
                                                                                startActivity(intent)
                                                                                finish()
                                                                                Toast.makeText(This, "Request deleted succesfully", Toast.LENGTH_LONG).show()
                                                                            }

                                                                            override fun onCancelled(error: DatabaseError) {
                                                                                TODO("Not yet implemented")
                                                                            }

                                                                        })
                                                                    }

                                                                    override fun onCancelled(error: DatabaseError) {
                                                                        TODO("Not yet implemented")
                                                                    }

                                                                })


                                                            })
                                                            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->

                                                            })
                                                            .setIcon(android.R.drawable.ic_delete)
                                                            .show()


                                                })
                                                ButtonTrace = Button(This)
                                                ButtonTrace.setText("Update Tracement")
                                                ButtonTrace.id=Integer.parseInt(snapshot.key.toString())
                                                ButtonTrace.setBackgroundResource(R.drawable.buttonblue)
                                                ButtonTrace.setTextColor(Color.WHITE)
                                                ButtonTrace.setPadding(50,0,50,0)

                                                ButtonTrace.setOnClickListener({b->ButtonTrace
                                                    var trace1: String = "Nothing yet"
                                                    trace1 = trace
                                                    AlertDialog.Builder(This)
                                                            .setTitle("Confirm Delete")
                                                            .setMessage("Are you sure that you want to update the tracement whit the following address: " + trace1 + "? ") // Specifying a listener allows you to take an action before dismissing the dialog.

                                                            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                                                ref.getReference("ShopRequest/${b.id}/tracement").setValue("Last Tracement: " + trace1)
                                                                Toast.makeText(This, "Tracement updated succesfully", Toast.LENGTH_LONG).show()
                                                            })
                                                            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->

                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_map)
                                                            .show()

                                                })
                                                ButtonDescription = Button(This)
                                                ButtonDescription.setText("Details")
                                                ButtonDescription.id=Integer.parseInt(snapshot.key.toString())
                                                ButtonDescription.setBackgroundResource(R.drawable.buttonblue)
                                                ButtonDescription.setTextColor(Color.WHITE)
                                                ButtonDescription.setPadding(10,0,10,0)
                                                ButtonDescription.setOnClickListener(View.OnClickListener {b->ButtonDescription
                                                    ref.getReference("/ShopRequest/${b.id}").addListenerForSingleValueEvent(object: ValueEventListener{
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            username = ""
                                                            shop = ""
                                                            tel = ""
                                                            description = ""
                                                            snapshot.children.forEach {
                                                                if (it.key.toString().equals("ntel"))
                                                                    tel = it.value.toString()
                                                                if (it.key.toString().equals("usernamereq"))
                                                                    username = it.value.toString()
                                                                if (it.key.toString().equals("shopps"))
                                                                    shop = it.value.toString()
                                                            }
                                                            description = "The shop is: " + shop + "Your Costumer is " + username + " tel. " + tel
                                                            AlertDialog.Builder(This)
                                                                    .setTitle("Request Description")
                                                                    .setMessage(description) // Specifying a listener allows you to take an action before dismissing the dialog.
                                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                                    .setPositiveButton("I've Read", DialogInterface.OnClickListener { dialog, which ->
                                                                        // Continue with delete operation
                                                                    })
                                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                                    .show()
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }

                                                    })



                                                })
                                                onerow.addView(CountView)
                                                onerow.addView(ButtonDescription,lb)
                                                onerow.addView(ButtonTrace,lb)
                                                onerow.addView(ButtonDelete,lb)
                                                onerow.setBackgroundResource(R.drawable.roundedrow)
                                                onerow.setPadding(10, 10, 10, 10)
                                                table.addView(onerow)
                                                lp.bottomMargin = 20

                                                onerow.layoutParams = lp
                                                onerow.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                                                onerow.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                    }


                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                prova = error.message
                Log.d("error", error.message)
            }


        })

    }
    override fun onLocationChanged(location: Location) {
        val ref=FirebaseDatabase.getInstance()
        trace=(CalculateLocation(location.latitude, location.longitude))


    }
    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    private fun CalculateLocation(latitude: Double, longitude: Double): String {
        val geocoder: Geocoder
        geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>
        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addresses[0].getAddressLine(0)

        return address

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.acceptedrequestes_menu, menu)
        return true
    }
    private fun logout (view: AcceptedRequestesActivity){
        FirebaseAuth.getInstance().signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout(this)
                Toast.makeText(this, "goodbye", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_wd -> {
                val intent = Intent(this, WDActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_myRequestes -> {
                val intent = Intent(this, MyRequestesActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}