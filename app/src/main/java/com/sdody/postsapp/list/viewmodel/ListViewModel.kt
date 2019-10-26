package com.sdody.postsapp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.extensions.toLiveData
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.State
import com.sdody.postsapp.constants.Constants.INITIAL_LOAD_SIZE_HINT
import com.sdody.postsapp.constants.Constants.PAGE_SIZE
import com.sdody.postsapp.list.di.PostDH
import com.sdody.postsapp.list.model.ListDataContract

import io.reactivex.disposables.CompositeDisposable

class ListViewModel(
    private val repo: ListDataContract.Repository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    //paging
    lateinit var postList: LiveData<PagedList<Post>>


    init {

        val factory: DataSource.Factory<Int, Post> = repo.allPosts()
        if (factory!=null)
        {
            val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setEnablePlaceholders(false)
                .build()
            postList = LivePagedListBuilder(factory, config).build()
        }

    }


    fun addPost(post: Post) {

        repo.addPost(post)
    }

    fun fetchPosts() {
        repo.getPostsFromRemote(0, PAGE_SIZE)
    }

    fun deletePost(post: Post) {

        repo.deletePost(post)
    }

    fun updatePost(post: Post) {

        repo.editPost(post)
    }

    fun getAddedCallback(): MutableLiveData<State> {
        return repo.postAddedCallback
    }

    fun getUpdatedCallback(): MutableLiveData<State> {
        return repo.postUpdatedCallback
    }

    fun getDeletedCallback(): MutableLiveData<State> {
        return repo.postDeletedCallback
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyListComponent()
    }
}