package com.example.rajgram.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rajgram.Adapter.MyPostAdapter
import com.example.rajgram.Models.Post
import com.example.rajgram.R
import com.example.rajgram.databinding.FragmentMyPostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class MyPostFragment : Fragment() {
    private lateinit var binding : FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =  FragmentMyPostBinding.inflate(layoutInflater, container, false)
        var postList = ArrayList<Post>()
        var adapter = MyPostAdapter(requireContext(), postList )
        binding.recycleView.layoutManager = StaggeredGridLayoutManager(
            3 , StaggeredGridLayoutManager.VERTICAL
        )
        binding.recycleView.adapter = adapter
        Firebase.firestore
            .collection(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
                var tempLIst = ArrayList<Post>()
                if (it.isEmpty){
                    Log.e("error" , "it is empty")
                }
                for ( i in it.documents){
                    var post : Post= i.toObject<Post>()!!
                    Log.e("posts" , post.toString())
                    tempLIst.add(post)
                }
                postList.addAll(tempLIst)



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