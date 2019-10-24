package com.sdody.postsapp.list.adapter

import com.sdody.postsapp.commons.data.local.Post

interface Interaction {
    fun postClicked(
        post: Post
    )
    fun postEdit(
        post:  Post?,position :Int
    )
    fun postDeleted(
        post:  Post?,position :Int
    )
}
