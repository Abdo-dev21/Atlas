package com.example.atlas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister.setOnClickListener {
            performRegistration()


        }

        alreadyHaveAccountRegister.setOnClickListener {
            Log.d("Main Activity", "Try to show login activity")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        buttonSelectPhoto.setOnClickListener {
            Log.d("RegisterActivity", "Try to show phot selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Register Activity","Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)


            imageViewRegister.setImageBitmap(bitmap)
            buttonSelectPhoto.alpha = 0f
        }

    }
    private fun performRegistration(){
        var email = emailEditTextRegister.text.toString()
        var password = passwordEditTextRegister.text.toString()
        var confPass = confpasswordEditTextRegister.text.toString()
        var firstname = firstnameEditTextRegister.text.toString()
        var lastname = lastnameEditTextRegister.text.toString()
        var idnum = ndocEditTextRegister.text.toString()
        var phone= phonenumberEditTextRegister.text.toString()
        var address=addressEditTextRegister.text.toString()

        if (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty()
            || idnum.isEmpty() || phone.isEmpty() || address.isEmpty()){
            Toast.makeText(this, "Please enter text in  fields", Toast.LENGTH_LONG).show()
            return
        }
        if(!password.equals(confPass)){
            Toast.makeText(this, "Please enter the same password in each field", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Main Activity", "Email is $email")
        Log.d("Main Activity", "Password is $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                // it's work
                uploadImageToFirebase()
                Log.d("Main Activity", "Utente Creato con successo ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user : ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Main Activity", "Failed to create user : ${it.message}")

            }
    }
    private fun saveUserToFirebaseDatabse(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var firstname = firstnameEditTextRegister.text.toString()
        var lastname = lastnameEditTextRegister.text.toString()
        var idnum = ndocEditTextRegister.text.toString()
        var phone= phonenumberEditTextRegister.text.toString()
        var address=addressEditTextRegister.text.toString()
        val user = User(uid, usernameEditTextRegister.text.toString(), profileImageUrl, firstname, lastname,
         idnum, phone, address,0,"false","true" )
        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"utentecreato con successo!",Toast.LENGTH_LONG).show()
                Log.d("Register Activity", "Utente salvato anche nel db")

                // start activity dopo aver creato l'utente
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Impossibile salvare l'utente nel db ${it.message}", Toast.LENGTH_LONG).show()
            }

    }
    private fun uploadImageToFirebase() {
        val image=findViewById<CircleImageView>(R.id.imageViewRegister)
        if (selectedPhotoUri == null) {
            selectedPhotoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/atlas-593ec.appspot.com/o/images%2F884537_blue_512x512.png?alt=media&token=885ed7cf-9e14-4458-92c9-41965e5bccad" )// location di dove l'immagine è stata memorizzata
            Picasso.get().load(selectedPhotoUri).into(image)
            saveUserToFirebaseDatabse(selectedPhotoUri.toString())

        }
        else{
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register Activity", "Immagine caricata con successo")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register Activity", "File Location $it")
                    saveUserToFirebaseDatabse(it.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Qualcosa è andato storto", Toast.LENGTH_LONG).show()
            }
        }
    }
}