package com.sdody.postsapp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sdody.postsapp.commons.data.local.Post
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


    var postList: LiveData<PagedList<Post>>
    var factory: DataSource.Factory<Int, Post> = repo.allPosts()

    init {


            val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setEnablePlaceholders(true)
                .build()
            postList = LivePagedListBuilder(repo.allPosts(), config).build()
    }

    fun getPostsNotSynced() {
        repo.getPostsNotSynced()
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

    fun getpostCrudCallback(): MutableLiveData<State> {
        return repo.postCrudCallback
    }


    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyListComponent()
    }

}