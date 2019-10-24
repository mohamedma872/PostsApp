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
import com.sdody.postsapp.list.model.ListDataContract

import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable) : ViewModel() {

    //for load data on demand
    val postsOutcome: LiveData<Outcome<List<Post>>> by lazy {
        //Convert publish subject to livedata
        repo.postFetchOutcome.toLiveData(compositeDisposable)
    }
    //paging
    // var postsDataSourceFactory: PostsDataSourceFactory
    var postList: LiveData<PagedList<Post>>
    init {
//        postsDataSourceFactory = PostsDataSourceFactory(repo)
//        val config = PagedList.Config.Builder()
//            .setPageSize(PAGE_SIZE)
//            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
//            .setEnablePlaceholders(false)
//            .build()
//        postList = LivePagedListBuilder(postsDataSourceFactory, config).build()

        val factory: DataSource.Factory<Int, Post> = repo.allPosts()
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setEnablePlaceholders(false)
            .build()
        postList = LivePagedListBuilder(factory, config).build()
    }


    fun getPosts() {
       if (postsOutcome.value == null)
          repo.getPostsFromRemote(1,PAGE_SIZE)
    }
    fun addPost(post:Post) {

            repo.addPost(post)
    }
    fun fetchPosts() {
    repo.getPostsFromRemote(1,PAGE_SIZE)
    }
    fun deletePost(post:Post) {

        repo.deletePost(post)
    }

    fun updatePost(post:Post) {

        repo.editPost(post)
    }
    fun getAddedCallback() :MutableLiveData<State>
    {
        return repo.postAddedCallback
    }
    fun getUpdatedCallback() :MutableLiveData<State>
    {
        return repo.postUpdatedCallback
    }
    fun getDeletedCallback() :MutableLiveData<State>
    {
        return repo.postDeletedCallback
    }
    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        //PostDH.destroyListComponent()
    }
}