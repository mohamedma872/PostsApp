package com.sdody.postsapp.list.model

import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.data.local.PostDb
import com.sdody.postsapp.commons.extensions.performOnBack
import com.sdody.postsapp.commons.networking.Scheduler
import io.reactivex.Completable
import io.reactivex.Flowable

class ListLocalData(private val postDb: PostDb, private val scheduler: Scheduler) : ListDataContract.Local {
    override fun EditPost(post: Post) {
        postDb.postDao().update(post)
    }

    override fun AddPost(post: Post) {
        postDb.postDao().insert(post)
    }

    override fun deletePost(post: Post) {
        postDb.postDao().delete(post)
    }

    override fun getPosts(): Flowable<List<Post>> {
        return postDb.postDao().getPosts()
    }

    override fun savedPosts(posts: List<Post>) {
        Completable.fromAction {
            postDb.postDao().insertall(posts)
        }
            .performOnBack(scheduler)
            .subscribe()
    }
}

