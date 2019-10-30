package com.sdody.postsapp.list.model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.addTo
import com.sdody.postsapp.commons.extensions.performOnBackOutOnMain
import com.sdody.postsapp.commons.networking.Scheduler
import com.sdody.postsapp.commons.networking.State
import io.reactivex.disposables.CompositeDisposable

class ListRepository(


    val local: ListDataContract.Local,
    val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {

    override val paginatedChatElements: DataSource.Factory<Int, Post> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        allPosts()
    }
    override val postCrudCallback: MutableLiveData<State> = MutableLiveData()
    override var listener: ((List<Post>) -> Unit)? = null


    override fun getPostsLocal(): List<Post> {
        return local.getPosts()
    }

    fun getaddedPostsNotSynced(post: Post) {
        if (post.opertaionType == 1) {
            // operation type 1 for insert new post
            remote.addPost(post).performOnBackOutOnMain(scheduler).subscribe {
                //to indicate that post is synced to server
                post.issynced = true
                local.editPost(post)
            }.addTo(compositeDisposable)
        }
    }

    fun getupdatedPostsNotSynced(post: Post) {
        if (post.opertaionType == 2) {
            // operation type 2 for update new post
            remote.editPost(post).performOnBackOutOnMain(scheduler).subscribe {
                //to indicate that post is synced to server
                post.issynced = true
                local.editPost(post)
            }.addTo(compositeDisposable)
        }
    }

    fun getdeletedPostsNotSynced(post: Post) {
        if (post.opertaionType == 3) {
            // operation type 3 for delete new post
            deletePost(post)
        }
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
                            getupdatedPostsNotSynced(post)
                            getupdatedPostsNotSynced(post)
                            getdeletedPostsNotSynced(post)
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
                when {
                    res != null -> when {
                        res.isNotEmpty() -> {
                            savedPosts(res)
                            getPostsFromRemote(page + pageSize, pageSize)
                        }
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

    override  fun deletePost(post: Post) {
            post.issynced = false
            post.opertaionType = 3
            local.editPost(post)
            remote.deletePost(post).performOnBackOutOnMain(scheduler).subscribe({
                local.deletePost(post)

                postCrudCallback.postValue(State.DONE)

            }, {
                //to update post in db that is not synced
                post.issynced = false
                post.opertaionType = 3
                local.editPost(post)
                //push updates to UI
                postCrudCallback.postValue(State.ERROR)
            }).addTo(compositeDisposable)


    }

    override fun editPost(post: Post) {
        local.editPost(post)
        remote.editPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postCrudCallback.postValue(State.DONE)
            //to indicate that post is synced to server
            post.issynced = true
            local.editPost(post)
        }, { postCrudCallback.postValue(State.ERROR) }).addTo(compositeDisposable)

    }

    override fun addPost(post: Post) {
        local.addPost(post)
        remote.addPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postCrudCallback.postValue(State.DONE)
            //to indicate that post is synced to server
            post.issynced = true
            local.editPost(post)
        }, { postCrudCallback.postValue(State.ERROR) }).addTo(compositeDisposable)

    }

    override fun handleError(error: Throwable) {
        //  postFetchOutcome.failed(error)
    }

}