package com.sdody.postsapp.list.adapter

import com.sdody.postsapp.commons.data.local.Post

interface Interaction {
    fun postClicked(
        holder:PostViewHolder
    )
    fun postEdit(
        post:  Post?,position :Int,holder:PostViewHolder
    )
    fun postDeleted(
        post:  Post?,position :Int,holder:PostViewHolder
    )
}
