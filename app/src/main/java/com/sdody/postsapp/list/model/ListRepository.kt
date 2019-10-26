package com.sdody.postsapp.list.model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.*
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.Scheduler
import com.sdody.postsapp.commons.networking.State
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class ListRepository(


    val local: ListDataContract.Local,
    val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {


    override val postAddedCallback: MutableLiveData<State> = MutableLiveData()
    override val postUpdatedCallback: MutableLiveData<State> = MutableLiveData()
    override val postDeletedCallback: MutableLiveData<State> = MutableLiveData()

    override var listener: ((List<Post>) -> Unit)? = null


    override fun getPostsLocal(): List<Post> {
        return local.getPosts()
    }

    override fun getPostsNotSynced() {
        // operation type 0 for default
        try {
            local.getPostsNotSynced()
                .performOnBackOutOnMain(scheduler)
                .subscribe({ posts ->
                    if(posts.isNotEmpty())
                    {
                        for (post in posts) {
                            if (post.opertaionType == 1) {
                                // operation type 1 for insert new post
                                remote.addPost(post).performOnBackOutOnMain(scheduler).subscribe {
                                    //to indicate that post is synced to server
                                    post.issynced = true
                                    local.editPost(post)
                                }.addTo(compositeDisposable)
                            }
                            if (post.opertaionType == 2) {
                                // operation type 2 for update new post
                                remote.editPost(post).performOnBackOutOnMain(scheduler).subscribe {
                                    //to indicate that post is synced to server
                                    post.issynced = true
                                    local.editPost(post)
                                }.addTo(compositeDisposable)
                            }
//                            if (post.opertaionType == 3) {
//                                // operation type 3 for delete new post
//                                deletePost(post)
//                            }
                        }
                    }

                }, { error -> handleError(error) })
                .addTo(compositeDisposable)
        }catch (ex:Exception)
        {
            Log.e("getPostsNotSynced",ex.message)
        }

    }

    override fun getPostsFromRemote(page: Int, pageSize: Int) {

        remote.getPosts(page, pageSize).performOnBackOutOnMain(scheduler)
            .subscribe({ res ->
                if (res != null) {

                    if (res.isNotEmpty()) {
                        //listener?.invoke(res)
                        savedPosts(res)
                        getPostsFromRemote(page + pageSize, pageSize)
                    }
                }

            },
                {


                })
            .addTo(compositeDisposable)

    }

    override fun allPosts(): DataSource.Factory<Int, Post> {
        return local.allPosts()
    }

    override fun savedPosts(posts: List<Post>) {

        posts.forEach { e -> e.issynced = true }
        local.savedPosts(posts)
    }

    override fun deletePost(post: Post) {
        remote.deletePost(post).performOnBackOutOnMain(scheduler).subscribe({
            local.deletePost(post)
            postDeletedCallback.postValue(State.DONE)

        }, {
            //to update post in db that is not synced
            post.issynced = false
            post.opertaionType = 3
            local.editPost(post)
            //push updates to UI
            postDeletedCallback.postValue(State.ERROR)
        }).addTo(compositeDisposable)

    }

    override fun editPost(post: Post) {
        local.editPost(post)
        remote.editPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postUpdatedCallback.postValue(State.DONE)
            //to indicate that post is synced to server
            post.issynced = true
            local.editPost(post)
        }, { postUpdatedCallback.postValue(State.ERROR) }).addTo(compositeDisposable)

    }

    override fun addPost(post: Post) {
        local.addPost(post)
        remote.addPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postAddedCallback.postValue(State.DONE)
            //to indicate that post is synced to server
            post.issynced = true
            local.editPost(post)
        }, { postAddedCallback.postValue(State.ERROR) }).addTo(compositeDisposable)

    }

    override fun handleError(error: Throwable) {
        //  postFetchOutcome.failed(error)
    }

}