package com.example.atlas



import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MyRequestesActivity : AppCompatActivity() {
    val context=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View = layoutInflater.inflate(R.layout.activity_myrequestes, null)
        setContentView(view)
        /*
        view.setOnClickListener(object : View.OnClickListener {
            var zoomFactor = 2f
            var zoomedOut = false
            override fun onClick(v: View) {
                if (zoomedOut) {
                    v.scaleX = 1f
                    v.scaleY = 1f
                    zoomedOut = false
                } else {
                    v.scaleX = zoomFactor
                    v.scaleY = zoomFactor
                    zoomedOut = true
                }
            }
        })*/
        var prova: String = ""
        val ref = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().uid
        val table = findViewById<TableLayout>(R.id.TableMyRequest)
        var rows: Long = 0
        var flag: Boolean
        var ids:String=""
        var description: String=""
        var TraceView: TextView
        var shop: String=""
        var i:Int=0
        var onerow: TableRow= TableRow(context)
        var CounterView: TextView
        var ButtonDelete: Button
        var ButtonDescription: Button
        var ButtonModify: Button
        var ButtonComplete: Button
        var trace: String = ""
        var username: String = ""
        var tel: String = ""
        var flag1:Boolean=false
        var points1: Int = 0
        var points2: Int = 0
        var count1:Int=0
        var ida:String=""
        var compratore=""
        var venditore=""
        var rowl=ArrayList<Int>()
        var uid1:String=""
        var lp: TableRow.LayoutParams
        var lb: TableRow.LayoutParams
        lb=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        lb.leftMargin=15
        var buttonFlag: Boolean
        var accepted: String=""
        var flag3:Boolean
        var count: Int = 0
        //var f:Boolean=true
        /*
        ref.getReference("/ShopRequest/").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                rows=snapshot.childrenCount
                f=false
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        while(f){
        }
        Toast.makeText(this, rows.toString(), Toast.LENGTH_LONG).show()*/

        ref.getReference("/ShopRequest/").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    Log.d("ids: ", it.key.toString())
                    ref.getReference("/ShopRequest/${it.key.toString()}")
                            .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            buttonFlag = true
                                            accepted = "not accepted yet"
                                            description = ""
                                            shop = ""
                                            flag3 = false
                                            flag = true
                                            snapshot.children.forEach {
                                                if (it.key.toString().equals("tracement")) {
                                                    trace = it.value.toString()
                                                    Log.d("trace: ", trace)

                                                }
                                                if (it.key.toString().equals("Shopper")) {
                                                    if (!it.value.toString().equals(""))
                                                        buttonFlag = false
                                                }
                                                if (it.key.toString().equals("uidp")) {

                                                    if (it.value.toString().equals(uid)) {
                                                        Log.d("trace: ", it.value.toString())
                                                        /*
                                                ref.getReference("/ShopRequest/${snapshot.key.toString()}").addValueEventListener(object:ValueEventListener{
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        snapshot.children.forEach{

                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }

                                                })*/
                                                        TraceView = TextView(context)
                                                        TraceView.setText(trace)
                                                        count++
                                                        CounterView = TextView(context)
                                                        CounterView.setText(count.toString())


                                                        ButtonDelete = Button(context)
                                                        ButtonDelete.setText("DELETE")
                                                        ButtonDelete.id =
                                                                Integer.parseInt(snapshot.key.toString())
                                                        ButtonDelete.setOnClickListener(View.OnClickListener { b ->
                                                            ButtonDelete
                                                            AlertDialog.Builder(context)
                                                                    .setTitle("Confirm Delete")
                                                                    .setMessage("Are you sure that you want to delete this request?")

                                                                    .setPositiveButton(
                                                                            "Yes",
                                                                            DialogInterface.OnClickListener { dialog, which ->
                                                                                ref.getReference("/ShopRequest/${b.id}")
                                                                                        .removeValue()
                                                                                var intent = Intent(
                                                                                        context,
                                                                                        MyRequestesActivity::class.java
                                                                                )
                                                                                startActivity(intent)
                                                                                finish()
                                                                                Toast.makeText(
                                                                                        context,
                                                                                        "Request deleted succesfully",
                                                                                        Toast.LENGTH_LONG
                                                                                ).show()
                                                                            })
                                                                    .setNegativeButton(
                                                                            "No",
                                                                            DialogInterface.OnClickListener { dialog, which ->

                                                                            })
                                                                    .setIcon(android.R.drawable.ic_delete)
                                                                    .show()


                                                        })

                                                        ButtonModify = Button(context)
                                                        ButtonModify.setText("MODIFY")
                                                        ButtonModify.id =
                                                                Integer.parseInt(snapshot.key.toString())
                                                        ButtonModify.setOnClickListener(View.OnClickListener { b ->
                                                            ButtonModify
                                                            val editShop = EditText(context)
                                                            ref.getReference("/ShopRequest/${b.id}")
                                                                    .addListenerForSingleValueEvent(object :
                                                                            ValueEventListener {
                                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                                            snapshot.children.forEach {
                                                                                if (it.key.toString()
                                                                                                .equals("shopps")
                                                                                ) {

                                                                                    editShop.hint =
                                                                                            it.value.toString()
                                                                                }
                                                                            }

                                                                        }

                                                                        override fun onCancelled(error: DatabaseError) {
                                                                            TODO("Not yet implemented")
                                                                        }

                                                                    })

                                                            var alert1 = AlertDialog.Builder(context)
                                                            alert1.setView(editShop)
                                                            alert1.setTitle("CHANGE YOUR SHOP")
                                                            alert1.setMessage("write the new shop")
                                                            alert1.setPositiveButton("Submit") { dialog, which ->

                                                                if (editShop.text.toString().equals(""))
                                                                    ref.getReference("/ShopRequest/${b.id}/shopps")
                                                                            .setValue(editShop.hint.toString())
                                                                else ref.getReference("/ShopRequest/${b.id}/shopps")
                                                                        .setValue(editShop.text.toString())
                                                                dialog.cancel()
                                                                dialog.dismiss()
                                                            }

                                                            alert1.setNegativeButton("No") { dialog, which ->
                                                                dialog.cancel()
                                                                dialog.dismiss()
                                                            }
                                                            alert1.create().show()
                                                        })

                                                        ButtonComplete = Button(context)
                                                        ButtonComplete.setText("COMPLETE")
                                                        ButtonComplete.id =
                                                                Integer.parseInt(snapshot.key.toString())
                                                        ButtonComplete.setOnClickListener(View.OnClickListener { b ->
                                                            ButtonComplete
                                                            AlertDialog.Builder(context)
                                                                    .setTitle("Terminate Request")
                                                                    .setMessage("Do you have receive your shop?" +
                                                                            " are you sure that you want to terminate this request?" +
                                                                            "the payment of 10 points will be made! ")

                                                                    .setPositiveButton(
                                                                            "Yes",
                                                                            DialogInterface.OnClickListener { dialog, which ->
                                                                                ref.getReference("/Ntf/cont1").addValueEventListener(object:ValueEventListener{
                                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                                                Log.d("CONTATORE", snapshot.value.toString())
                                                                                                count1=Integer.parseInt(snapshot.value.toString())
                                                                                                count1++
                                                                                    }

                                                                                    override fun onCancelled(error: DatabaseError) {
                                                                                        TODO("Not yet implemented")
                                                                                    }

                                                                                })
                                                                                ref.getReference("/ShopRequest/${b.id}").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                                        snapshot.children.forEach {
                                                                                            if(it.key.toString().equals("usernamereq")){
                                                                                                username=it.value.toString()
                                                                                            }
                                                                                            if(it.key.toString().equals("shopps")){
                                                                                                shop=it.value.toString()
                                                                                            }
                                                                                            if (it.key.toString().equals("uidp")) {
                                                                                                ref.getReference("/users/${it.value.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                                                        snapshot.children.forEach {
                                                                                                            if (it.key.toString().equals("points")) {
                                                                                                                points1 = Integer.parseInt(it.value.toString()) - 10
                                                                                                                compratore = snapshot.key.toString()

                                                                                                            }
                                                                                                        }

                                                                                                        ref.getReference("/users/$compratore/points").setValue(points1)
                                                                                                    }

                                                                                                    override fun onCancelled(error: DatabaseError) {
                                                                                                    }

                                                                                                })
                                                                                            }
                                                                                            if (it.key.toString().equals("Shopper")) {
                                                                                                ref.getReference("/users/${it.value.toString()}").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                                                        snapshot.children.forEach {
                                                                                                            if (it.key.toString().equals("points")) {
                                                                                                                points2 = Integer.parseInt(it.value.toString()) + 10
                                                                                                                venditore = snapshot.key.toString()

                                                                                                            }
                                                                                                        }
                                                                                                       // ref.getReference("/users/$venditore/completeflag").setValue("true")
                                                                                                        ref.getReference("/Ntf/$venditore/$count1").setValue("You have completed the following request: of "+username+", "+shop+"." +
                                                                                                                "You have obtained 10 points")
                                                                                                        ref.getReference("/Ntf/cont1").setValue(count1)

                                                                                                        ref.getReference("/users/$venditore/points").setValue(points2)
                                                                                                    }

                                                                                                    override fun onCancelled(error: DatabaseError) {

                                                                                                    }

                                                                                                })
                                                                                            }

                                                                                        }


                                                                                    }

                                                                                    override fun onCancelled(error: DatabaseError) {

                                                                                    }
                                                                                })


                                                                                ref.getReference("/ShopRequest/${b.id}").removeValue()
                                                                                var intent = Intent(context, MyRequestesActivity::class.java)
                                                                                startActivity(intent)
                                                                                finish()
                                                                                Toast.makeText(context, "Request completed successfully", Toast.LENGTH_LONG).show()
                                                                                dialog.cancel()
                                                                                dialog.dismiss()
                                                                            })
                                                                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                                                                        dialog.cancel()
                                                                        dialog.dismiss()
                                                                    })
                                                                    .setIcon(android.R.drawable.ic_menu_send)
                                                                    .show()


                                                        })

                                                        ButtonDescription = Button(context)
                                                        ButtonDescription.setText("Details")
                                                        ButtonDescription.id =
                                                                Integer.parseInt(snapshot.key.toString())
                                                        ButtonDescription.setOnClickListener(View.OnClickListener { b ->
                                                            ButtonDescription

                                                            ref.getReference("/ShopRequest/${b.id}")
                                                                    .addListenerForSingleValueEvent(object :
                                                                            ValueEventListener {
                                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                                            snapshot.children.forEach {
                                                                                if (it.key.toString()
                                                                                                .equals("tracement")
                                                                                ) {
                                                                                    trace = it.value.toString()
                                                                                }
                                                                                if (it.key.toString()
                                                                                                .equals("shopps")
                                                                                ) {
                                                                                    shop = it.value.toString()
                                                                                    description =
                                                                                            "Your Shop is: " + shop + ", " + accepted
                                                                                }
                                                                                if (it.key.toString().equals("Shopper")) {
                                                                                    if (!it.value.toString().equals("")) {
                                                                                        flag3 = true
                                                                                        ida = it.value.toString()
                                                                                    }
                                                                                }
                                                                            }
                                                                            ref.getReference("/users/$ida").addValueEventListener(object : ValueEventListener {
                                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                                    snapshot.children.forEach {
                                                                                        if (it.key.toString().equals("ntel"))
                                                                                            tel = it.value.toString()
                                                                                        if (it.key.toString().equals("username"))
                                                                                            username = it.value.toString()
                                                                                    }
                                                                                    if (flag3) {
                                                                                        Log.d("usernametel: ", username + " " + tel)
                                                                                        accepted = "is accepted by " + username + " tel. " + tel
                                                                                    } else {
                                                                                        accepted = "not accepted yet"
                                                                                    }
                                                                                    flag3 = false
                                                                                    description =
                                                                                            "Your Shop is: " + shop + ", " + accepted
                                                                                    var alert =
                                                                                            AlertDialog.Builder(context)
                                                                                    alert.setTitle("Request Description")
                                                                                    alert.setMessage(description)
                                                                                    alert.setPositiveButton(
                                                                                            "I've Read",
                                                                                            DialogInterface.OnClickListener { dialog, which ->
                                                                                                dialog.cancel()
                                                                                                dialog.dismiss()
                                                                                            })
                                                                                    alert.setIcon(android.R.drawable.ic_dialog_info)
                                                                                    alert.create().show()
                                                                                }

                                                                                override fun onCancelled(
                                                                                        error: DatabaseError
                                                                                ) {
                                                                                }

                                                                            })


                                                                        }

                                                                        override fun onCancelled(error: DatabaseError) {
                                                                            TODO("Not yet implemented")
                                                                        }

                                                                    })

                                                        })
                                                        onerow = TableRow(context)

                                                        onerow.addView(CounterView)
                                                        CounterView.setTextColor(Color.WHITE)
                                                        CounterView.setTextSize(20.0f)
                                                        CounterView.setPadding(10, 10, 10, 10)
                                                        TraceView.setTextColor(Color.WHITE)
                                                        TraceView.setPadding(10,0,10,0)
                                                        ButtonDescription.setBackgroundResource(R.drawable.buttonblue)
                                                        ButtonDescription.setTextColor(Color.WHITE)
                                                        ButtonDescription.setPadding(10,0,10,0)
                                                        onerow.addView(ButtonDescription,lb)
                                                        onerow.addView(TraceView)
                                                        for (i in 0..(rowl.size - 1)) {
                                                            if (rowl.get(i) == Integer.parseInt(snapshot.key.toString()))
                                                                flag = false
                                                        }
                                                        if (buttonFlag) {
                                                            onerow.addView(ButtonModify,lb)
                                                            onerow.addView(ButtonDelete,lb)
                                                            ButtonModify.setBackgroundResource(R.drawable.buttonblue)
                                                            ButtonDelete.setBackgroundResource(R.drawable.buttonblue)
                                                            ButtonModify.setTextColor(Color.WHITE)
                                                            ButtonModify.setPadding(10,0,10,0)
                                                            ButtonDelete.setTextColor(Color.WHITE)
                                                            ButtonDelete.setPadding(10,0,10,0)

                                                        } else {
                                                            onerow.addView(ButtonComplete,lb)
                                                            ButtonComplete.setBackgroundResource(R.drawable.buttonblue)
                                                            ButtonComplete.setTextColor(Color.WHITE)
                                                            ButtonComplete.setPadding(10,0,10,0)
                                                        }
                                                        if (flag) {
                                                            onerow.setBackgroundResource(R.drawable.roundedrow)
                                                            onerow.setPadding(10, 10, 10, 10)

                                                            table.addView(onerow, i)
                                                            lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                                                            lp.bottomMargin = 20
                                                            onerow.layoutParams = lp
                                                            onerow.layoutParams.width = MATCH_PARENT
                                                            onerow.layoutParams.height = WRAP_CONTENT

                                                            rowl.add(Integer.parseInt(snapshot.key.toString()))
                                                            i++
                                                        }


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

    private fun logout(view: MyRequestesActivity) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.myrequestes_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout(this)
                Toast.makeText(this, "goodbye", Toast.LENGTH_SHORT).show()
                finish()
                true
            }

            R.id.menu_wd -> {
                val intent = Intent(this, WDActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_acceptedRequestes -> {
                val intent = Intent(this, AcceptedRequestesActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
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



