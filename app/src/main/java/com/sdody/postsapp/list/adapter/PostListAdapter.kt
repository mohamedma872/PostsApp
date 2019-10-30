package com.sdody.postsapp.list.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sdody.postsapp.commons.data.local.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostListAdapter
    : PagedListAdapter<Post, RecyclerView.ViewHolder>(NewsDiffCallback) {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        (holder as PostViewHolder).bind(getItem(position))

        holder.itemView.btnDelete.setOnClickListener {
            interaction?.postDeleted(

                holder
            )

        }
        holder.itemView.btnEdit.setOnClickListener {
            interaction?.postEdit(

                holder
            )
        }
        holder.itemView.setOnClickListener {

            interaction?.postClicked(

                holder
            )
        }
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.postId == newItem.postId
            }
        }
    }

    fun getElementItem(position: Int): Post {
        return getItem(position)!!
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 0
    }


}