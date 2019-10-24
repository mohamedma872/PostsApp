package com.sdody.postsapp.list.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.list.model.ListDataContract


class PostsDataSourceFactory(
    private val repo: ListDataContract.Repository)
    : DataSource.Factory<Int, Post>() {
    private val newsDataSourceLiveData = MutableLiveData<PostsDataSource>()
    override fun create(): DataSource<Int, Post> {
        val postsDataSource = PostsDataSource(repo)
        newsDataSourceLiveData.postValue(postsDataSource)
        return postsDataSource
    }
}