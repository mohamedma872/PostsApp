package com.sdody.postsapp.list.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.sdody.postsapp.commons.data.local.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() , View.OnClickListener{
    var interaction: Interaction? = null
    private val mDiffer = AsyncPagedListDiffer(this, DIFF_CALLBACK)
    override fun getItemCount(): Int {
        return mDiffer.itemCount
    }

    fun submitList(pagedList: PagedList<Post>) {
        mDiffer.submitList(pagedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.itemView.tag = position
        val user = mDiffer.getItem(position)
        if (user != null) {
            // holder.bindTo(user);
            holder.bind(user)
            holder.itemView.btnDelete.setOnClickListener {
                val ID= mDiffer.getItem(position)!!.postId
                val tittle = mDiffer.getItem(position)!!.postTitle
                Log.e("btnDelete","pos $position postID $ID  posttittle $tittle")
                interaction?.postDeleted(
                    mDiffer.getItem(position),
                    holder.itemView.tag as Int
                )
            }
            holder.itemView.btnEdit.setOnClickListener {
                val ID= mDiffer.getItem(position)!!.postId
                val tittle = mDiffer.getItem(position)!!.postTitle
                Log.e("btnEdit","pos $position postID $ID  posttittle $tittle")
                interaction?.postEdit(
                    mDiffer.getItem(position),
                    holder.itemView.tag as Int
                )
            }
            holder.itemView.setOnClickListener(this)

        } else {
            // Null defines a placeholder item - AsyncPagedListDiffer will automatically
            // invalidate this row when the actual object is loaded from the database
            // holder.clear();
        }
    }
    override fun onClick(v: View?) {
        val clicked =  mDiffer.getItem(v!!.tag as Int)
        if (clicked != null) {
            interaction?.postClicked(clicked)
        }
    }
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(
                oldUser: Post, newUser: Post
            ): Boolean {
                // User properties may have changed if reloaded from the DB, but ID is fixed
                return oldUser.postId == newUser.postId
            }

            override fun areContentsTheSame(
                oldUser: Post, newUser: Post
            ): Boolean {
                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                return oldUser == newUser
            }
        }
    }

    fun updateItem(post: Post?, position: Int) {
        this.notifyItemChanged(position, post)
    }

    fun deleteItem(position: Int) {
        this.notifyItemRemoved(position)
    }
}