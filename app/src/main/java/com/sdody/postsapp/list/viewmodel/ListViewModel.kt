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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListViewModel(
    private val repo: ListDataContract.Repository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    //paging

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val paginatedChatElements = repo.paginatedChatElements

    lateinit var postList: LiveData<PagedList<Post>>
    var factory: DataSource.Factory<Int, Post>

    init {

         factory = paginatedChatElements
        if (factory!=null)
        {
            val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setEnablePlaceholders(true)
                .build()
            postList = LivePagedListBuilder(repo.allPosts(), config).build()

        }


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
        uiScope.launch {
            repo.deletePost(post)
        }
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
        viewModelJob.cancel()
    }
    fun invalidateDataSource() {

       postList.value?.dataSource?.invalidate()


    }
}