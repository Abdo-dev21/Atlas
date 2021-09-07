package com.example.atlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            performLogin()
        }

        backToRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performLogin(){
        var email = emailEditTextLogin.text.toString()
        var password = passwordEditTextLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please insert username/password", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Login Activity", "Login with email: $email and password $password")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("LoginActivity", "Login effettuato con utente ${it.result?.user?.uid}")
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                // Toast
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login Fallito ${it.message}", Toast.LENGTH_LONG).show()
                // Log.d
            }

    }
}