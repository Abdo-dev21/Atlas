package com.example.atlas


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class RequestShoppingActivity : AppCompatActivity(), LocationListener {


    protected var locationManager: LocationManager? = null

    protected var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_shopping)
        val EditShop= findViewById<EditText>(R.id.ShoppingEditText)
        val EditDelivery= findViewById<EditText>(R.id.deliverypointEditText)
        EditDelivery.hint=""
        val button=findViewById<Button>(R.id.buttonSend)
        var points1:Int=0
        var count:Int=0
        val uid=FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance()
        var ntel:String=""
        var username:String=""
        ref.getReference("").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if(it.key.toString().equals("cont")){
                        count=Integer.parseInt(it.getValue().toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        ref.getReference("/users/$uid").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if(it.key.toString().equals("points")){
                        points1=Integer.parseInt(it.getValue().toString())
                    }
                    if(it.key.toString().equals("ntel")){
                        ntel=it.getValue().toString()
                    }
                    if(it.key.toString().equals("username")){
                        username=it.getValue().toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        button.setOnClickListener(View.OnClickListener{ v->

            if(!EditDelivery.text.toString().equals("")){
                EditDelivery.hint=EditDelivery.text
            }
            if(EditShop.text.toString().equals("")||EditDelivery.hint.toString().equals("")){
                Toast.makeText(this,"Please fill the request's fields", Toast.LENGTH_LONG).show()
            }
            else{

                val editShop = EditText(v.context)
                val editDel=EditText(v.context)
                editShop.hint=EditShop.text.toString()
                if(EditDelivery.text.toString().equals(""))
                    editDel.hint=EditDelivery.hint.toString()
                else editDel.hint=EditDelivery.text.toString()
                val ShopDialog = AlertDialog.Builder(v.context)
                //
                EditDelivery.hint=""
                val layout = LinearLayout(v.context)
                layout.orientation = LinearLayout.VERTICAL
                layout.addView(editDel)
                layout.addView(editShop) // Another add method


                ShopDialog.setView(layout)
                ShopDialog.setTitle("Confirm your data!")
                ShopDialog.setMessage("The request costs 10 points")
                ShopDialog.setPositiveButton("Submit") { dialog, which ->
                    if(points1>=10) {
                        val uid = FirebaseAuth.getInstance().uid
                        val ref = FirebaseDatabase.getInstance()
                        ref.getReference("/ShopRequest/$count/id").setValue(count)
                        ref.getReference("/ShopRequest/$count/uidp").setValue(uid)
                        if(editDel.text.toString().equals(""))
                            ref.getReference("/ShopRequest/$count/address").setValue(editDel.hint.toString())
                        else ref.getReference("/ShopRequest/$count/address").setValue(editDel.text.toString())
                        if (editShop.text.toString().equals(""))
                            ref.getReference("/ShopRequest/$count/shopps").setValue(editShop.hint.toString())
                        else ref.getReference("/ShopRequest/$count/shopps").setValue(editShop.text.toString())
                        ref.getReference("/ShopRequest/$count/ntel").setValue(ntel)
                        ref.getReference("/ShopRequest/$count/usernamereq").setValue(username)
                        ref.getReference("/ShopRequest/$count/flag").setValue("false")
                        ref.getReference("/ShopRequest/$count/tracement").setValue("Last Tracement: Nothing yet")
                        ref.getReference("/ShopRequest/$count/Shopper").setValue("")
                        ref.getReference("/users/$uid/points").setValue(points1 - 1)
                        ref.getReference("/cont").setValue(count+1)
                        Toast.makeText(
                                this@RequestShoppingActivity,
                                "Your request has been submited! You have spent 10 points.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(
                                this@RequestShoppingActivity,
                                "You have not enough points!",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                ShopDialog.setNegativeButton("No") { dialog, which ->
                    // close
                }
                ShopDialog.create().show()
            }
        })

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



    }


    override fun onLocationChanged(location: Location) {
        var Editdelivery = findViewById<TextView>(R.id.deliverypointEditText)

        Editdelivery.setHint(CalculateLocation(location.latitude, location.longitude))


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
        menuInflater.inflate(R.menu.requestshopping_menu, menu)
        return true
    }
    private fun logout (view: RequestShoppingActivity){
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