package com.sdody.postsapp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.toLiveData
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.list.model.ListDataContract

import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val postsOutcome: LiveData<Outcome<List<Post>>> by lazy {
        //Convert publish subject to livedata
        repo.postFetchOutcome.toLiveData(compositeDisposable)
    }

    fun getPosts() {
        if (postsOutcome.value == null)
            repo.fetchPosts()
    }
    fun AddPost(post:Post) {

            repo.AddPost(post)
    }
    fun DeletePost(post:Post) {

        repo.deletePost(post)
    }

    fun UpdatePost(post:Post) {

        repo.EditPost(post)
    }
    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        //PostDH.destroyListComponent()
    }
}