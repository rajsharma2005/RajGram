package com.example.rajgram.Posts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rajgram.HomeActivity
import com.example.rajgram.Models.Post
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.POST
import com.example.rajgram.Utills.POST_FOLDER
import com.example.rajgram.Utills.USER_NODE
import com.example.rajgram.Utills.USER_PROFILE_FOLDER
import com.example.rajgram.Utills.uploadImage
import com.example.rajgram.databinding.ActivityPostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class PostActivity : AppCompatActivity() {
    private var binding : ActivityPostBinding? = null

    var imageUrl : String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        uri?.let {
            uploadImage(uri , POST_FOLDER){
                url ->
                if (url != null){

                    binding!!.postImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        binding!!.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this , HomeActivity::class.java))

            finish()
        }

        binding!!.postImage.setOnClickListener{
            launcher.launch("image/*")
        }

        binding!!.PostBtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE)
                .document(Firebase.auth.currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()!!
            val post: Post = Post(
                postUrl = imageUrl!!,
                caption = binding!!.caption.editText?.text.toString(),
                uid = Firebase.auth.currentUser!!.email.toString() ,
                time = System.currentTimeMillis().toString()

            )

            Firebase.firestore.collection(POST)
                .document()
                .set(post)
                .addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.email.toString())
                        .document()
                        .set(post)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    finish()
                }
        }
        }
        binding!!.cancelBtn.setOnClickListener{
            startActivity(Intent(this , HomeActivity::class.java))
            finish()
        }
    }
}