package com.example.rajgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rajgram.Models.User
import com.example.rajgram.Utills.USER_NODE
import com.example.rajgram.Utills.USER_PROFILE_FOLDER
import com.example.rajgram.Utills.uploadImage
import com.example.rajgram.databinding.ActivitySignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class SignInActivity : AppCompatActivity() {

    private var binding : ActivitySignInBinding? = null
     lateinit var user : User
     private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
         uri ->
         uri?.let {
              uploadImage(uri , USER_PROFILE_FOLDER){
                 if (it != null){
                     user.image = it
                     binding!!.userProfileImage.setImageURI(uri)
                 }
             }
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        user = User()

        //getting intent from profile Fragment for the edit profile
        if (intent.hasExtra("MODE")){
            if (intent.getIntExtra("MODE" , -1) == 1){
                binding!!.signUpBtn.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE)
                    .document(Firebase.auth.currentUser!!.email.toString())
                    .get()
                    .addOnSuccessListener {
//
                       user = it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get()
                                .load(user.image)
                                .into(binding!!.userProfileImage)
                        }
                        binding!!.nameInput.editText?.setText(user.name)
                        binding!!.emailInput.editText?.setText(user.email)


                        }
                    }
            }


        val text = "<font color = #FF000000>Already have an account , </font> <font color = #1E88E5>login</font>"
        binding!!.loginBtn.text = Html.fromHtml(text)
        binding!!.signUpBtn.setOnClickListener{
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE" , -1) == 1){
                    user.name = binding!!.nameInput.editText!!.text.toString()

                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.email.toString())
                        .set(user)
                        .addOnSuccessListener {

                        startActivity(
                            Intent(
                                this@SignInActivity,
                                HomeActivity::class.java
                            )
                        )
                        finish()

                    }
                }
            }else {

                if (binding!!.nameInput.editText!!.text.toString().equals("") or
                    binding!!.emailInput.editText!!.text.toString().equals("") or
                    binding!!.passwordInput.editText!!.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Please fill the information",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(
                            binding!!.emailInput.editText!!.text.toString(),
                            binding!!.passwordInput.editText!!.text.toString()
                        ).addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                user.name = binding!!.nameInput.editText!!.text.toString()
                                user.email = binding!!.emailInput.editText!!.text.toString()
                                user.password = binding!!.passwordInput.editText!!.text.toString()

                                Firebase.firestore.collection(USER_NODE)
                                    //this is where we have use the email id
                                    //if there will be any issue with it in future with it
                                    //you can use uid
                                    //code for that
                                    //Firebase.auth.currentUser!!.uid
                                    .document(Firebase.auth.currentUser!!.email.toString())
                                    .set(user)
                                    .addOnSuccessListener {
                                        startActivity(
                                            Intent(
                                                this@SignInActivity,
                                                HomeActivity::class.java
                                            )
                                        )
                                        finish()

                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    result.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

        binding!!.addImage.setOnClickListener{
            launcher.launch("image/*")
        }

        binding!!.loginBtn.setOnClickListener {
            var intent = Intent(this , LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}