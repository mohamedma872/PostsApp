package com.sdody.postsapp.list.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.State
import com.sdody.postsapp.list.model.ListDataContract
import io.reactivex.Completable
import io.reactivex.functions.Action

class PostsDataSource(
    private val repo: ListDataContract.Repository
) : PageKeyedDataSource<Int, Post>() {

   private var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Post>
    ) {
        updateState(State.LOADING)
        repo.listener ={lst->
            callback.onResult(
                lst,
                null,
                1)
        }
        repo.getPostsFromRemote(1,params.requestedLoadSize)


    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        updateState(State.LOADING)
        repo.listener ={lst->
            callback.onResult(
                lst,
                params.key+params.requestedLoadSize)
        }
        repo.getPostsFromRemote(params.key+params.requestedLoadSize,params.requestedLoadSize)


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

//    fun retry() {
//        if (retryCompletable != null) {
////            compositeDisposable.add(
////                retryCompletable!!
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe()
////            )
//        }
//    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}