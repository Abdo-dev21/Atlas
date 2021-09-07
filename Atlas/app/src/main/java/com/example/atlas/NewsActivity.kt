package com.example.atlas


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.CalendarContract
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.view.Gravity.CENTER
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
import kotlinx.android.synthetic.main.activity_home.*


class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_news)
        val context=this
        val Table=findViewById<TableLayout>(R.id.TableNews)
        val ref=FirebaseDatabase.getInstance()
        val uid=FirebaseAuth.getInstance().uid
        val button=findViewById<Button>(R.id.deletentfbtn)
        var textview:TextView
        var onerow:TableRow
        var i:Int=0
        var lp:LinearLayout.LayoutParams
        lp = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        lp.width= MATCH_PARENT
        lp.height= WRAP_CONTENT
        var lb: TableRow.LayoutParams

        lb=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)

        lb.weight= 1.0f

        lb.height= WRAP_CONTENT
        ref.getReference("/Ntf/$uid").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    onerow= TableRow(context)

                    textview= TextView(context)

                    onerow.setBackgroundResource(R.drawable.roundedrow1)
                    onerow.setPadding(10,10,10,10)
                    onerow.addView(textview,lb)
                    textview.setText(it.value.toString())

                    textview.setTextSize(15f)
                    textview.setTextColor(Color.BLACK)
                    textview.setTypeface(Typeface.DEFAULT_BOLD)


                    Table.addView(onerow,i)

                    lp.bottomMargin=20
                    lp.leftMargin=10
                    lp.rightMargin=10
                    onerow.layoutParams=lp
                    textview.maxWidth=1000

                    i++
                    

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        button.setOnClickListener({
            AlertDialog.Builder(context)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure that you want to delete all news?")

                    .setPositiveButton(
                            "Yes",
                            DialogInterface.OnClickListener { dialog, which ->
                                ref.getReference("/Ntf/$uid").removeValue()
                                var intent = Intent(context, NewsActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(context, "You have deleted all the news", Toast.LENGTH_LONG).show()
                            })
                    .setNegativeButton(
                            "No",
                            DialogInterface.OnClickListener { dialog, which ->

                            })
                    .setIcon(android.R.drawable.ic_delete)
                    .show()
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_menu, menu)
        return true
    }
    private fun logout (view: NewsActivity){
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