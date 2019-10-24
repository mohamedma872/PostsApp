package com.sdody.postsapp.list.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sdody.postsapp.commons.data.local.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostListAdapter()
    : PagedListAdapter<Post, RecyclerView.ViewHolder>(NewsDiffCallback), View.OnClickListener {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(getItem(position))
        holder.itemView.btnDelete.setOnClickListener { showDeleteDialog(holder,getItem(position)) }
        holder.itemView.btnEdit.setOnClickListener { showUpdateDialog(holder, getItem(position)) }
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
        return super.getItemCount() +  0
    }
    override fun onClick(v: View?) {
        val clicked = getItem(v!!.tag as Int)
        if (clicked != null) {
            interaction?.postClicked(clicked)
        }
    }

    fun showDeleteDialog(holder: PostViewHolder, post: Post?) {
        val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Confirm delete?")
        dialogBuilder.setPositiveButton("Delete", { dialog, whichButton ->
         //   deleteMovie(movie)
            if (post != null) {

            }
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }
    fun showUpdateDialog(holder: PostViewHolder, post: Post?) {
        val dialogBuilder = AlertDialog.Builder(holder.itemView.context)

        val input = EditText(holder.itemView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        if (post != null) {
            input.setText(post.postTitle)
        }

        dialogBuilder.setView(input)

        dialogBuilder.setTitle("Update Post")
        dialogBuilder.setPositiveButton("Update", { dialog, whichButton ->
           // updateMovie(Movie(movie.id,input.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}