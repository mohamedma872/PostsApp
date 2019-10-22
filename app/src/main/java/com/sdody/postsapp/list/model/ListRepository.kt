package com.sdody.postsapp.list.model


import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.*
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class ListRepository(
    private val local: ListDataContract.Local,
    private val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {
    override val postFetchOutcome: PublishSubject<Outcome<List<Post>>>
      = PublishSubject.create<Outcome<List<Post>>>()
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
        remote.deletePost(post)
        local.deletePost(post)
    }

    override fun EditPost(post: Post) {
        remote.EditPost(post)
        local.EditPost(post)
    }

    override fun AddPost(post: Post) {
        remote.AddPost(post)
        local.AddPost(post)
    }

    override fun handleError(error: Throwable) {
        postFetchOutcome.failed(error)
    }

}