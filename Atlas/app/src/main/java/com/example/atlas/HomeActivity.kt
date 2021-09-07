package com.example.atlas


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyUserLoggedIn()
        setContentView(R.layout.activity_home)


        passive_users_button.setOnClickListener {
            val intent = Intent(this, RequestShoppingActivity::class.java)
            startActivity(intent)


        }
        active_users_button.setOnClickListener {
            val intent = Intent(this, PendingRequestActivity::class.java)
            startActivity(intent)
        }

    }
    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }
    private fun logout (view: HomeActivity){
        FirebaseAuth.getInstance().signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_logout -> {
                logout(this)
                Toast.makeText(this, "goodbye", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_wd -> {
                val intent = Intent(this, WDActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_acceptedRequestes-> {
                val intent = Intent(this, AcceptedRequestesActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_myRequestes -> {
                val intent = Intent(this, MyRequestesActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
