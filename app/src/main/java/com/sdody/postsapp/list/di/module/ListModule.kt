package com.sdody.postsapp.list.di.module

import android.content.Context
import androidx.room.Room
import com.sdody.postsapp.commons.data.local.PostDb
import com.sdody.postsapp.commons.data.local.getDatabase
import com.sdody.postsapp.commons.data.remote.PostService
import com.sdody.postsapp.commons.networking.Scheduler
import com.sdody.postsapp.constants.Constants
import com.sdody.postsapp.list.di.ListScope
import com.sdody.postsapp.list.model.ListDataContract
import com.sdody.postsapp.list.model.ListLocalData
import com.sdody.postsapp.list.model.ListRemoteData
import com.sdody.postsapp.list.model.ListRepository
import com.sdody.postsapp.list.adapter.PostListAdapter
import com.sdody.postsapp.list.viewmodel.ListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@Module
@ListScope
class ListModule {




    /*Adapter*/
    @Provides
    @ListScope
    fun provideadapter(): PostListAdapter =
        PostListAdapter()

    /*ViewModel*/
    @Provides
    @ListScope
    fun providelistViewModelFactory(repository: ListDataContract.Repository, compositeDisposable: CompositeDisposable): ListViewModelFactory = ListViewModelFactory(repository,compositeDisposable)


    /*Repository*/
    @Provides
    @ListScope
    fun providelistRepo(local: ListDataContract.Local, remote: ListDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable): ListDataContract.Repository = ListRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @ListScope
    fun provideremoteData(postService: PostService): ListDataContract.Remote = ListRemoteData(postService)

    @Provides
    @ListScope
    fun providelocalData(postDb: PostDb, scheduler: Scheduler): ListDataContract.Local = ListLocalData(postDb, scheduler)

    @Provides
    @ListScope
    fun providecompositeDisposable(): CompositeDisposable = CompositeDisposable()


    /*Parent providers to dependents*/
    @Provides
    @ListScope
    fun providepostDb(context: Context): PostDb
        {
            return getDatabase(context)
        }

    @Provides
    @ListScope
    fun providepostService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)
}