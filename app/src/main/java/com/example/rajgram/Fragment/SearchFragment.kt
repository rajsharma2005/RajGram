package com.example.rajgram.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajgram.Adapter.SearchAdapter
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.USER_NODE
import com.example.rajgram.databinding.FragmentSearchBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.recyclerView.adapter = adapter

        Firebase.firestore.collection(USER_NODE)
            .get()
            .addOnSuccessListener {
                userList.clear()
                var tempList = ArrayList<User>()
                for (i in it.documents) {
                    if (i.id.toString() == Firebase.auth.currentUser!!.email.toString()) {
                       // Toast.makeText(requireContext(), "It is equel", Toast.LENGTH_SHORT).show()
                    } else {
                        var user: User = i.toObject<User>()!!

                        tempList.add(user)
                    }

                }
                userList.addAll(tempList)
                adapter.notifyDataSetChanged()

            }

        binding.searchView.setOnClickListener {
            var text  = binding.searchView.text.toString()

            Firebase.firestore.collection(USER_NODE)
                .whereEqualTo("name" , text)
                .get()
                .addOnSuccessListener {
                    userList.clear()
                    var tempList = ArrayList<User>()
                    for (i in it.documents) {
                        if (i.id.toString() == Firebase.auth.currentUser!!.email.toString() || it.isEmpty) {
                            // Toast.makeText(requireContext(), "It is equel", Toast.LENGTH_SHORT).show()
                        } else {
                            var user: User = i.toObject<User>()!!

                            tempList.add(user)
                        }

                    }
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
        }




        return binding.root
    }

    companion object {

    }
}