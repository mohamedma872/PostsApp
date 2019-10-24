package com.sdody.postsapp.list.adapter

import com.sdody.postsapp.commons.data.local.Post

interface Interaction {
    fun postClicked(
        post: Post
    )
}
