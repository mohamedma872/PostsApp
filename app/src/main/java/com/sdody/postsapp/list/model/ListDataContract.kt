package com.sdody.postsapp.list.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.State
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {

    interface Repository {
        var listener: ((List<Post>)->Unit)?
       // val postFetchOutcome: PublishSubject<Outcome<List<Post>>>
        val postAddedCallback:  MutableLiveData<State>
        val postUpdatedCallback:  MutableLiveData<State>
        val postDeletedCallback:  MutableLiveData<State>
        fun getPostsFromRemote(page: Int,pageSize: Int)
        fun getPostsLocal():Flowable<List<Post>>
        fun allPosts(): DataSource.Factory<Int, Post>
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun editPost( post: Post)
        fun addPost( post: Post)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getPosts(): Flowable<List<Post>>
        fun allPosts(): DataSource.Factory<Int, Post>
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun editPost( post: Post)
        fun addPost( post: Post)
    }

    interface Remote {
        fun getPosts(page: Int,pageSize: Int):Single<List<Post>>
        fun deletePost( post: Post):Completable
        fun editPost( post: Post):Completable
        fun addPost( post: Post):Completable
    }
}