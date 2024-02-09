package com.example.rajgram.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rajgram.Adapter.ViewPagerAdapter
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.SignInActivity
import com.example.rajgram.Utills.USER_NODE
import com.example.rajgram.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
   private lateinit var binding : FragmentProfileBinding
   private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater , container , false)

        binding.editProfile.setOnClickListener{
            val intent = Intent(activity , SignInActivity::class.java)
            intent.putExtra("MODE" , 1)
            activity?.startActivity(intent)
            activity?.finish()

        }

        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment() , "My Post")
        viewPagerAdapter.addFragments(MyReelFragment() , "My Reels")
        binding.viewPager.adapter = viewPagerAdapter
        binding.tab.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()

       // Log.i("document " , Firebase.auth.currentUser!!.email.toString())
        Firebase.firestore.collection(USER_NODE)
            .document(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
//               if (it == null){
//                   Log.e("error in Success" ,"it is null")
//               }else {
//                   val user: User = it.toObject<User>()!!
//                   binding.name.text = user.name
//                   binding.bio.text = user.email
//               }
                if (it.exists()) {
                    val user: User? = it.toObject(User::class.java)
                    if (user != null) {
                        binding.name.text = user.name
                        binding.bio.text = user.email
                        if (!user.image.isNullOrEmpty()){
                            Picasso.get()
                                .load(user.image)
                                .into(binding!!.profileIMage)
                        }
                    } else {
                        Log.e("error", "User is null")
                    }
                } else {
                    Log.e("error", "Document does not exist")
                }
            }
            /*
            if (documentSnapshot.exists()) {
                val user: User? = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    binding.name.text = user.name
                    binding.bio.text = user.email
                }
             */
            .addOnFailureListener {

                Log.e("error" , it.message.toString())
            }
    }
}