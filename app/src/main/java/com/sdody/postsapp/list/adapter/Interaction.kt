package com.sdody.postsapp.list.adapter

interface Interaction {
    fun postClicked(
        holder:PostViewHolder
    )
    fun postEdit(
        holder: PostViewHolder
    )
    fun postDeleted(
        holder: PostViewHolder
    )
}
