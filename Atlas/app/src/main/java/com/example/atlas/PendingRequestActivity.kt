package com.example.atlas

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.cos
import kotlin.math.sqrt


class PendingRequestActivity: AppCompatActivity(), LocationListener {
    protected var locationManager: LocationManager? = null
    protected var locationListener: LocationListener? = null
    var lat:Double=0.0
    var long:Double=0.0
    var lat1:Double=0.0
    var long1:Double=0.0
    val This=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendingrequest)
        val table=findViewById<TableLayout>(R.id.TableRequest)
        var location:List<Address>
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance()
        var rows:Int=0
        var distance:Double=0.0
        val coder = Geocoder(this.getApplicationContext())
        var prova:String="null"
        var distanceView:TextView
        var username:String=""
        var shop:String
        var count1=0
        var tel:String
        var address:String=""
        var i:Int=0
        var description:String
        var ButtonDescription:Button
        var username1=""
        var button:Button
        var compratore:String
        var count:Int=0
        var row=ArrayList<TableRow>()
        var distances=ArrayList<Double>()
        var onerow:TableRow
        var CountView:TextView
        var DelLat:Double
        var DelLong:Double
        var lp:TableRow.LayoutParams
        var lb: TableRow.LayoutParams
        lb=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        lb.leftMargin=300

        //Toast.makeText(this,prova+" "+rows.toString()+" "+uid,Toast.LENGTH_LONG).show()
        var flag:Boolean
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

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
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

        ref.getReference("/ShopRequest/").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach{
                    ref.getReference("/ShopRequest/${it.key.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            snapshot.children.forEach {
                                if (it.key.toString().equals("uidp")) {
                                    if (!it.value.toString().equals(uid)) {
                                        compratore=it.value.toString()
                                        ref.getReference("/ShopRequest/${snapshot.key.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                snapshot.children.forEach{
                                                    if(it.key.toString().equals("Shopper")){
                                                        if(it.value.toString().equals("")){
                                                            username1=""
                                                            shop=""
                                                            ref.getReference("/ShopRequest/${snapshot.key.toString()}").addListenerForSingleValueEvent(object: ValueEventListener{
                                                                override fun onDataChange(snapshot: DataSnapshot) {

                                                                    snapshot.children.forEach{
                                                                        if(it.key.toString().equals("address")){
                                                                            address=it.value.toString()
                                                                        }
                                                                        if(it.key.toString().equals("shopps")){
                                                                            shop=it.value.toString()
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
                                                                    onerow.layoutParams = lp
                                                                    onerow.addView(CountView)
                                                                    ButtonDescription = Button(This)
                                                                    ButtonDescription.setText("Details")
                                                                    ButtonDescription.id=Integer.parseInt(snapshot.key.toString())
                                                                    ButtonDescription.setBackgroundResource(R.drawable.buttonblue)
                                                                    ButtonDescription.setTextColor(Color.WHITE)
                                                                    ButtonDescription.setPadding(10,0,10,0)
                                                                    ButtonDescription.setOnClickListener(View.OnClickListener {b->ButtonDescription
                                                                        ref.getReference("/ShopRequest/${b.id}").addListenerForSingleValueEvent(object: ValueEventListener{
                                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                                address=""
                                                                                username=""
                                                                                shop = ""
                                                                                tel = ""
                                                                                description = ""
                                                                                snapshot.children.forEach {
                                                                                    if (it.key.toString().equals("usernamereq")) {
                                                                                        username = it.getValue().toString()
                                                                                    }
                                                                                    if (it.key.toString().equals("shopps")) {
                                                                                        shop = it.getValue().toString()
                                                                                    }
                                                                                    if (it.key.toString().equals("ntel")) {
                                                                                        tel = it.getValue().toString()
                                                                                    }
                                                                                    if (it.key.toString().equals("address")) {
                                                                                        address = it.getValue().toString()
                                                                                    }
                                                                                }
                                                                                description="username: "+username+", shop: "+shop+", ntel: "+tel+", address: "+address+"."
                                                                                AlertDialog.Builder(This)
                                                                                        .setTitle("Request Description")
                                                                                        .setMessage(description) // Specifying a listener allows you to take an action before dismissing the dialog.
                                                                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                                                                        .setPositiveButton("I've Read", DialogInterface.OnClickListener { dialog, which ->
                                                                                            // Continue with delete operation
                                                                                        })
                                                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                        .show()
                                                                            }

                                                                            override fun onCancelled(error: DatabaseError) {
                                                                                TODO("Not yet implemented")
                                                                            }

                                                                        })



                                                                    })
                                                                    onerow.addView(ButtonDescription)
                                                                    button=Button(This)
                                                                    button.setText("Get Request!")
                                                                    button.id=Integer.parseInt(snapshot.key.toString())
                                                                    button.setBackgroundResource(R.drawable.buttonblue)
                                                                    button.setTextColor(Color.WHITE)
                                                                    button.setPadding(10,0,10,0)
                                                                    button.setOnClickListener(View.OnClickListener{b->button
                                                                        val ShopDialog = AlertDialog.Builder(This)

                                                                        ShopDialog.setTitle("Confirm getting request!")
                                                                        ShopDialog.setMessage("Are you sure to accept this request?")
                                                                        ShopDialog.setPositiveButton("yes") { dialog, which ->
                                                                            ref.getReference("/ShopRequest/${b.id}/flag").setValue("true")
                                                                            //ref.getReference("/ShopRequest/${b.id}/acquireflag").setValue("true")
                                                                            ref.getReference("/Ntf").addListenerForSingleValueEvent(object:ValueEventListener{
                                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                                    snapshot.children.forEach{
                                                                                        if(it.key.toString().equals("cont1")){
                                                                                            count1=Integer.parseInt(it.value.toString())
                                                                                            count1++
                                                                                        }
                                                                                    }
                                                                                    ref.getReference("/ShopRequest/${b.id}/Shopper").setValue(uid.toString())

                                                                                    ref.getReference("/users/$uid").addListenerForSingleValueEvent(object: ValueEventListener{
                                                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                                                            snapshot.children.forEach{

                                                                                                if(it.key.toString().equals("username")){

                                                                                                    username1=it.value.toString()
                                                                                                    ref.getReference("/Ntf/$compratore/$count1").setValue("Your request ("+shop+ ") was accepted by "+username1)
                                                                                                    ref.getReference("/Ntf/cont1").setValue(count1)

                                                                                                    Toast.makeText(This,"You got the request!",Toast.LENGTH_LONG).show()
                                                                                                    var intent = Intent(This, PendingRequestActivity::class.java)
                                                                                                    startActivity(intent)
                                                                                                    finish()
                                                                                                }
                                                                                            }
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



                                                                        }
                                                                        ShopDialog.setNegativeButton("No") { dialog, which ->
                                                                            // close
                                                                        }
                                                                        ShopDialog.create().show()
                                                                    })

                                                                    onerow.addView(button,lb)
                                                                    //row.add(onerow)
                                                                    onerow.setBackgroundResource(R.drawable.roundedrow)
                                                                    onerow.setPadding(10, 10, 10, 10)
                                                                    table.addView(onerow,i)
                                                                    lp.bottomMargin = 20
                                                                    onerow.layoutParams = lp
                                                                    onerow.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                                                                    onerow.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                                                    i++

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
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }



            }
            override fun onCancelled(error: DatabaseError) {
                prova=error.message
                Log.d("error",error.message)
            }
        })
    }



    override fun onLocationChanged(location: Location) {
        lat=location.latitude
        long=location.longitude

    }
    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pendingrequest_menu, menu)
        return true
    }
    private fun logout (view: PendingRequestActivity){
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
            R.id.menu_acceptedRequestes-> {
                val intent = Intent(this, AcceptedRequestesActivity::class.java)
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







