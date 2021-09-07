package com.example.atlas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_withdrawdeposit.*

class WDActivity : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().uid
    val ref= FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var points1:Int=0
        setContentView(R.layout.activity_withdrawdeposit)
        val np=findViewById<TextView>(R.id.npointsView)
        ref.getReference("/users/$uid").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if(it.key.toString().equals("points")){
                         points1=Integer.parseInt(it.getValue().toString())
                         np.setText(points1.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        Depositbutton.setOnClickListener(View.OnClickListener { v ->
            val getPoints = EditText(v.context)
            val DepositDialog = AlertDialog.Builder(v.context)
            DepositDialog.setTitle("Deposit")
            DepositDialog.setMessage("Insert how many money you want exchange for point, you will gain 10 point for each euro unit")
            DepositDialog.setView(getPoints)
            DepositDialog.setPositiveButton("Deposit") { dialog, which ->
                val TotMoney: Int = Integer.parseInt(getPoints.text.toString())
                var points: Int = TotMoney * 10
                ref.getReference("/users/$uid/points").setValue(points+ points1)


                Toast.makeText(
                    this@WDActivity,
                    "You have obtained "+points+" points!",
                    Toast.LENGTH_SHORT
                ).show()
                }

            DepositDialog.setNegativeButton("No") { dialog, which ->
                // close
            }
            DepositDialog.create().show()
        })
        Withdrawbutton.setOnClickListener(View.OnClickListener { v ->
            val setPoints = EditText(v.context)
            val WithdrawDialog = AlertDialog.Builder(v.context)

            WithdrawDialog.setTitle("Withdraw")
            WithdrawDialog.setMessage("Insert how many points you want to exchange in money, " +
                    "remember that you will obtain 1 euro for each 10 points")
            WithdrawDialog.setView(setPoints)
            WithdrawDialog.setPositiveButton("Withdraw") { dialog, which -> // extract the email and send reset link

                if(Integer.parseInt(setPoints.text.toString())>points1) {
                    Toast.makeText(
                        this@WDActivity,
                        "you can't exchange more points than you own",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    ref.getReference("/users/$uid/points").setValue(points1-Integer.parseInt(setPoints.text.toString()))
                    Toast.makeText(
                        this@WDActivity,
                        "You have exchange "+Integer.parseInt(setPoints.text.toString())+" into money!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            WithdrawDialog.setNegativeButton("No") { dialog, which ->
                // close
            }
            WithdrawDialog.create().show()
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.withdrawdeposit_menu, menu)
        return true
    }
    private fun logout (view: WDActivity){
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