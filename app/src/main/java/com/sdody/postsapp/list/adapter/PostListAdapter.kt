package com.sdody.postsapp.list.adapter

import android.util.Log
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
            val ID= getItem(position)!!.postId
            val tittle = getItem(position)!!.postTitle
            Log.e("btnDelete","pos $position postID $ID  posttittle $tittle")
            interaction?.postDeleted(
                getItem(position),
                holder.itemView.tag as Int,holder
            )

        }
        holder.itemView.btnEdit.setOnClickListener {
            val ID= getItem(position)!!.postId
            val tittle = getItem(position)!!.postTitle
            Log.e("btnEdit","pos $position postID $ID  posttittle $tittle")
            interaction?.postEdit(
                getItem(position),
                holder.itemView.tag as Int
            )
        }
        holder.itemView.setOnClickListener(this)
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