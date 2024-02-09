package com.example.rajgram.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rajgram.Adapter.MyPostAdapter
import com.example.rajgram.Adapter.MyReelAdapter
import com.example.rajgram.Models.Post
import com.example.rajgram.Models.Reel
import com.example.rajgram.R
import com.example.rajgram.Utills.REEL
import com.example.rajgram.databinding.FragmentMyReelBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class MyReelFragment : Fragment() {

    private lateinit var binding: FragmentMyReelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelBinding.inflate(inflater, container, false)

        var reelList = ArrayList<Reel>()
        var adapter = MyReelAdapter(requireContext(), reelList  )
        binding.recycleView.layoutManager = StaggeredGridLayoutManager(
            3 , StaggeredGridLayoutManager.VERTICAL
        )
        binding.recycleView.adapter = adapter
        Firebase.firestore
            .collection(Firebase.auth.currentUser!!.email.toString() + REEL)
            .get()
            .addOnSuccessListener {
                var tempLIst = ArrayList<Reel>()
                if (it.isEmpty){
                    Log.e("error" , "it is empty")
                }
                for ( i in it.documents){
                    var post : Reel = i.toObject<Reel>()!!
                    Log.e("posts" , post.toString())
                    tempLIst.add(post)
                }
                reelList.addAll(tempLIst)



                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("error " , it.localizedMessage)
            }
        return binding.root
    }

    companion object {

    }
}