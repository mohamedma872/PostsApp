package com.sdody.postsapp.list.model


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


class ListRepository(


     val local: ListDataContract.Local,
     val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {

    override val postAddedCallback: MutableLiveData<State> = MutableLiveData()
    override val postUpdatedCallback: MutableLiveData<State> = MutableLiveData()
    override val postDeletedCallback: MutableLiveData<State> = MutableLiveData()
    override val postFetchOutcome: PublishSubject<Outcome<List<Post>>> =
        PublishSubject.create<Outcome<List<Post>>>()
    override var listener: ((List<Post>)->Unit)? = null


    override fun getPostsLocal(): Flowable<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPostsFromRemote(page: Int, pageSize: Int) {

        remote.getPosts(page, pageSize).performOnBackOutOnMain(scheduler)
            .subscribe({ res ->
if (res !=null)
{

    if (res.isNotEmpty())
    {
        listener?.invoke(res)
        savedPosts(res)
        getPostsFromRemote(page+pageSize,pageSize)
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
        local.savedPosts(posts)
    }

    override fun deletePost(post: Post) {
        local.deletePost(post)
        remote.deletePost(post).performOnBackOutOnMain(scheduler).subscribe({

            postDeletedCallback.postValue(State.DONE)

        }, {

            postDeletedCallback.postValue(State.ERROR)
        }) .addTo(compositeDisposable)

    }

    override fun editPost(post: Post) {
        local.editPost(post)
        remote.editPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postUpdatedCallback.postValue(State.DONE)
        }, { postUpdatedCallback.postValue(State.ERROR) }) .addTo(compositeDisposable)

    }

    override fun addPost(post: Post) {
        local.addPost(post)
        remote.addPost(post).performOnBackOutOnMain(scheduler).subscribe({
            postAddedCallback.postValue(State.DONE)
        }, { postAddedCallback.postValue(State.ERROR) }) .addTo(compositeDisposable)

    }

    override fun handleError(error: Throwable) {
        postFetchOutcome.failed(error)
    }

}