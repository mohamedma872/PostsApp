package com.sdody.postsapp.list.model

import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.data.local.PostDb
import com.sdody.postsapp.commons.extensions.performOnBack
import com.sdody.postsapp.commons.networking.Scheduler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ListLocalData(private val postDb: PostDb, private val scheduler: Scheduler) : ListDataContract.Local {
    override fun getPostsNotSynced(): Single<List<Post>> {
        return  postDb.postDao().getPostsNotSynced()
    }

    override fun allPosts(): DataSource.Factory<Int, Post> {
      return  postDb.postDao().alPosts()
    }

    override fun editPost(post: Post) {
        postDb.postDao().update(post)
    }

    override fun addPost(post: Post) {
        postDb.postDao().insert(post)
    }

    override fun deletePost(post: Post) {
        postDb.postDao().delete(post.postId!!)
    }

    override fun getPosts(): List<Post> {
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

