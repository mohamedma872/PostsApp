package com.sdody.postsapp.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdody.postsapp.R
import com.sdody.postsapp.commons.data.local.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostViewHolder(view: View) : RecyclerView.ViewHolder(view)  {


    fun bind(post: Post?) {
        when {
            post != null ->
            itemView.tittle.text = post.postTitle
        }
    }

    companion object {
        fun create(parent: ViewGroup): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return PostViewHolder(view)
        }
    }
}