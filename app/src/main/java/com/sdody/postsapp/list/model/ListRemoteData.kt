package com.sdody.postsapp.list.model

import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.data.remote.PostService
import io.reactivex.Single

class ListRemoteData(private val postService: PostService) : ListDataContract.Remote{
    override fun EditPost(post: Post) {
        postService.updatePost(post.postId,post)
    }

    override fun AddPost(post: Post) {
        postService.addPost(post)
    }

    override fun deletePost(post: Post) {
        postService.deletePost(post.postId)
    }

    override fun getPosts(): Single<List<Post>> {
        return postService.getPosts()
    }


}