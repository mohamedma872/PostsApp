package com.sdody.postsapp.list.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.State
import io.reactivex.Completable
import io.reactivex.Single

interface ListDataContract {

    interface Repository {
        val paginatedChatElements: DataSource.Factory<Int, Post>
        var listener: ((List<Post>)->Unit)?
        val postCrudCallback: MutableLiveData<State>

        fun getPostsFromRemote(page: Int,pageSize: Int)
        fun getPostsLocal():List<Post>
        fun allPosts(): DataSource.Factory<Int, Post>
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun editPost( post: Post)
        fun addPost( post: Post)
        fun handleError(error: Throwable)
        fun getPostsNotSynced()
    }

    interface Local {
        fun getPosts(): List<Post>
        fun allPosts(): DataSource.Factory<Int, Post>
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun editPost( post: Post)
        fun addPost( post: Post)
        fun getPostsNotSynced(): Single<List<Post>>
    }

    interface Remote {
        fun getPosts(page: Int,pageSize: Int):Single<List<Post>>
        fun deletePost( post: Post):Completable
        fun editPost( post: Post):Completable
        fun addPost( post: Post):Completable
    }
}