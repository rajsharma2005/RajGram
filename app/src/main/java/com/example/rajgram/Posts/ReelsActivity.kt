package com.example.rajgram.Posts

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rajgram.HomeActivity
import com.example.rajgram.Models.Post
import com.example.rajgram.Models.Reel
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.POST
import com.example.rajgram.Utills.POST_FOLDER
import com.example.rajgram.Utills.REEL
import com.example.rajgram.Utills.REEL_FOLDER
import com.example.rajgram.Utills.USER_NODE
import com.example.rajgram.Utills.uploadImage
import com.example.rajgram.Utills.uploadReel
import com.example.rajgram.databinding.ActivityReelsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelsActivity : AppCompatActivity() {
    private var binding : ActivityReelsBinding ? = null
    private lateinit var videoUrl : String
    lateinit var progressDialog : ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        uri?.let {
            uploadReel(uri ,REEL_FOLDER,progressDialog){
                    url ->
                if (url != null){


                    videoUrl = url
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog(this)

        binding!!.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this , HomeActivity::class.java))

            finish()
        }
        binding!!.selectBtn.setOnClickListener{
            launcher.launch("video/*")
        }

        binding!!.cancelBtn.setOnClickListener{
            startActivity(Intent(this , HomeActivity::class.java))
            finish()
        }

        binding!!.PostBtn.setOnClickListener{
            Firebase.firestore.collection(USER_NODE)
                .document(Firebase.auth.currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {
                    var user  : User= it.toObject<User>()!!
                    val reel : Reel = Reel(videoUrl!!,binding!!.caption.editText?.text.toString() , user.image!!)

                    Firebase.firestore.collection(REEL)
                        .document()
                        .set(reel)
                        .addOnSuccessListener {
                            Firebase.firestore.collection(Firebase.auth.currentUser!!.email.toString()+ REEL)
                                .document()
                                .set(reel)
                                .addOnSuccessListener {
                                    startActivity(Intent(this , HomeActivity::class.java))
                                    finish()
                                }
                            finish()
                        }
                }



        }
    }
}