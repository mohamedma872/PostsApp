package com.sdody.postsapp.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sdody.postsapp.commons.data.local.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostListAdapter
    : PagedListAdapter<Post, RecyclerView.ViewHolder>(NewsDiffCallback), View.OnClickListener {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        (holder as PostViewHolder).bind(getItem(position))

        holder.itemView.btnDelete.setOnClickListener {
            interaction?.postDeleted(
                getItem(position),
                position
            )
        }
        holder.itemView.btnEdit.setOnClickListener {
            interaction?.postEdit(
                getItem(position),
                position
            )
        }
        holder.itemView.setOnClickListener(this)
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 0
    }

    override fun onClick(v: View?) {
        val clicked = getItem(v!!.tag as Int)
        if (clicked != null) {
            interaction?.postClicked(clicked)
        }
    }

    fun updateItem(post: Post?, position: Int) {
        this.notifyItemChanged(position, post)
    }

    fun deleteItem(position: Int) {
        this.notifyItemRemoved(position)
    }

}