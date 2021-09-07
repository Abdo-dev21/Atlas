package com.example.atlas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.storage.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.imageViewUpdate
import kotlinx.android.synthetic.main.activity_register.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfileActivity: AppCompatActivity() {
    var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val username=findViewById<EditText>(R.id.usernameEditTextUpdate)
        val phone=findViewById<EditText>(R.id.phonenumberEditTextUpdate)
        val address=findViewById<EditText>(R.id.addressEditTextUpdate)
        val image=findViewById<CircleImageView>(R.id.imageViewUpdate)
        val imageButton=findViewById<Button>(R.id.buttonSelectPhotoUpdate)
        val uid = FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid/")

        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if(it.key.toString().equals("username")){
                        username.setHint(it.getValue().toString())
                    }
                    else{
                        if(it.key.toString().equals("ntel")){
                            phone.setHint(it.getValue().toString())
                        }
                        else if(it.key.toString().equals("address"))
                            address.setHint(it.getValue().toString())
                        else if(it.key.toString().equals("profileImageUrl")) {
                            selectedPhotoUri = Uri.parse(it.getValue().toString())
                            Picasso.get().load(selectedPhotoUri).into(image)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        } )
        buttonUpdate.setOnClickListener{
            performUpdate()
        }
        buttonSelectPhotoUpdate.setOnClickListener{
            Log.d("RegisterActivity", "Try to show phot selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val ref1=FirebaseStorage.getInstance()
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Register Activity","Photo was selected")
            ref1.getReferenceFromUrl(selectedPhotoUri.toString()).delete()

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)


            imageViewUpdate.setImageBitmap(bitmap)
            buttonSelectPhotoUpdate.alpha = 0f
            val filename=UUID.randomUUID().toString()
            ref1.getReference("images/$filename").putFile(selectedPhotoUri!!).addOnSuccessListener {
                ref1.getReference("images/$filename").downloadUrl.addOnSuccessListener {
                    Log.d("Register Activity", "File Location $it")
                    selectedPhotoUri=Uri.parse(it.toString())
                    Toast.makeText(this, "Foto uploaded successfully", Toast.LENGTH_LONG).show()

                }
                    .addOnFailureListener{
                        Toast.makeText(this, "Qualcosa è andato storto", Toast.LENGTH_LONG).show()
                    }
            }
        }

    }


    private fun performUpdate(){
        val username=findViewById<EditText>(R.id.usernameEditTextUpdate)
        val phone=findViewById<EditText>(R.id.phonenumberEditTextUpdate)
        val address=findViewById<EditText>(R.id.addressEditTextUpdate)

        val uid = FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance()
        if(!username.text.toString().equals(""))
            ref.getReference("/users/$uid/username").setValue(username.text.toString())
        if(!address.text.toString().equals(""))
            ref.getReference("/users/$uid/address").setValue(address.text.toString())
        if(!phone.text.toString().equals(""))
            ref.getReference("/users/$uid/ntel").setValue(phone.text.toString())
        ref.getReference("/users/$uid/profileImageUrl").setValue(selectedPhotoUri.toString())
        var intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()

    }


    private fun logout (view: ProfileActivity){
        FirebaseAuth.getInstance().signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
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




