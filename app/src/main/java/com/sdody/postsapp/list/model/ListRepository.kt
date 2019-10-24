package com.sdody.postsapp.list.model


import androidx.lifecycle.MutableLiveData
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.*
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.Scheduler
import com.sdody.postsapp.commons.networking.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class ListRepository(
    private val local: ListDataContract.Local,
    private val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {
    override val PostAddedCallback: MutableLiveData<State> = MutableLiveData()
    override val PostUpdatedCallback: MutableLiveData<State> = MutableLiveData()
    override val PostDeletedCallback: MutableLiveData<State> = MutableLiveData()
    override val postFetchOutcome: PublishSubject<Outcome<List<Post>>> =
        PublishSubject.create<Outcome<List<Post>>>()

    override fun getPosts() {
        postFetchOutcome.loading(true)
        local.getPosts().performOnBackOutOnMain(scheduler)
            .doAfterNext {

            }
            .subscribe({ posts ->
                postFetchOutcome.success(posts)
            }, { error -> handleError(error) })
            .addTo(compositeDisposable)
    }

    override fun savedPosts(posts: List<Post>) {
        local.savedPosts(posts)
    }

    override fun deletePost(post: Post) {
        local.deletePost(post)
        remote.deletePost(post).performOnBackOutOnMain(scheduler).subscribe({
            PostDeletedCallback.postValue(State.DONE)
        }, { PostDeletedCallback.postValue(State.ERROR) }) .addTo(compositeDisposable)

    }

    override fun EditPost(post: Post) {
        local.EditPost(post)
        remote.EditPost(post).performOnBackOutOnMain(scheduler).subscribe({
            PostUpdatedCallback.postValue(State.DONE)
        }, { PostUpdatedCallback.postValue(State.ERROR) }) .addTo(compositeDisposable)

    }

    override fun AddPost(post: Post) {
        local.AddPost(post)
        remote.AddPost(post).performOnBackOutOnMain(scheduler).subscribe({
            PostAddedCallback.postValue(State.DONE)
        }, { PostAddedCallback.postValue(State.ERROR) }) .addTo(compositeDisposable)

    }

    override fun handleError(error: Throwable) {
        postFetchOutcome.failed(error)
    }

}