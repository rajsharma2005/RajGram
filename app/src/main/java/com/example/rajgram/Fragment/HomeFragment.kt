package com.example.rajgram.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajgram.Adapter.FollowAdapter
import com.example.rajgram.Adapter.PostAdapter
import com.example.rajgram.Models.Post
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.FOLLOW
import com.example.rajgram.Utills.POST
import com.example.rajgram.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private var postList = mutableListOf<Post>()
    private lateinit var adapter : PostAdapter
    private var followList = ArrayList<User>()
    private lateinit var followAdapter: FollowAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext() , postList.asReversed())
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

        binding.recycleView.adapter = adapter
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        followAdapter = FollowAdapter(requireContext() , followList = followList)
        binding.followRv.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)

        binding.followRv.adapter = followAdapter


        Firebase.firestore
            .collection(Firebase.auth.currentUser!!.email+ FOLLOW)
            .get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                followList.clear()


                for (i in it.documents){
                    var user = i.toObject<User>()!!
                    tempList.add(user)
                }

                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }

        Firebase.firestore.collection(POST)
            .get()
            .addOnSuccessListener {
                var templist = mutableListOf<Post>()
                for( i in it.documents){
                    var post : Post = i.toObject<Post>()!!
                    templist.add(post)

                }
                postList.addAll(templist)
                adapter.notifyDataSetChanged()
            }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {

    }
}