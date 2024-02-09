package com.example.rajgram.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.FOLLOW
import com.example.rajgram.databinding.SearchRvBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SearchAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isfollow = false
        Glide.with(context)
            .load(userList[position].image)
            .placeholder(R.drawable.user)
            .into(holder.binding.profileIMage)

        holder.binding.name.text = userList[position].name

        Firebase.firestore
            .collection(Firebase.auth.currentUser!!.email + FOLLOW)
            .whereEqualTo("email", userList[position].email)
            .get()
            .addOnSuccessListener {
                if (it.documents.size == 0) {

                    isfollow = false
                } else {
                    holder.binding.follow.text = "Unfollow"
                    isfollow = true
                }
            }
        holder.binding.follow.setOnClickListener {
            if (isfollow) {
                Firebase.firestore
                    .collection(Firebase.auth.currentUser!!.email + FOLLOW)
                    .whereEqualTo("email", userList[position].email)
                    .get()
                    .addOnSuccessListener {
                        Firebase.firestore
                            .collection(Firebase.auth.currentUser!!.email + FOLLOW)
                            .document(it.documents.get(0).id)
                            .delete()
                        holder.binding.follow.text = "Follow"
                        isfollow = false
                    }
            } else {
                Firebase.firestore
                    .collection(Firebase.auth.currentUser!!.email + FOLLOW)
                    .document()
                    .set(userList.get(position))
                holder.binding.follow.text = "UnFollow"
                isfollow = true
            }
        }
    }
}