package com.example.rajgram.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rajgram.Models.Post
import com.example.rajgram.Models.User
import com.example.rajgram.R
import com.example.rajgram.Utills.USER_NODE

import com.example.rajgram.databinding.PostRvBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class PostAdapter (var context: Context, var postList : MutableList<Post> ) : RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(var binding: PostRvBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context) ,
            parent , false)

        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
       return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            Firebase.firestore.collection(USER_NODE)
                .document(postList[position].uid)
                .get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()
                    Glide.with(context)
                        .load(user!!.image)
                        .placeholder(R.drawable.user)
                        .into(holder.binding.profileIMage)

                    holder.binding.name.text = user.name

                }
        }catch (e : Exception){

        }
        Glide.with(context)
            .load(postList[position].postUrl)
            .placeholder(R.drawable.loading)
            .into(holder.binding.post)

        try {
            val text  = TimeAgo.using(postList.get(position).time.toLong())
            holder.binding.time.text = text
        }catch (e : Exception){
            holder.binding.time.text = ""
        }


        holder.binding.caption.text = postList[position].caption
        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.heartlike)
        }

        holder.binding.share.setOnClickListener {
            var intent  = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(Intent.EXTRA_TEXT , postList[position].postUrl)
            context.startActivity(intent)
        }

    }
}