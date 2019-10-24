package com.sdody.postsapp.list.model

import androidx.lifecycle.MutableLiveData
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.State
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {

    interface Repository {
        val postFetchOutcome: PublishSubject<Outcome<List<Post>>>
        val PostAddedCallback:  MutableLiveData<State>
        val PostUpdatedCallback:  MutableLiveData<State>
        val PostDeletedCallback:  MutableLiveData<State>
        fun getPosts()
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun EditPost( post: Post)
        fun AddPost( post: Post)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getPosts(): Flowable<List<Post>>
        fun savedPosts( posts: List<Post>)
        fun deletePost( post: Post)
        fun EditPost( post: Post)
        fun AddPost( post: Post)
    }

    interface Remote {
        fun getPosts(): Single<List<Post>>
        fun deletePost( post: Post):Completable
        fun EditPost( post: Post):Completable
        fun AddPost( post: Post):Completable
    }
}