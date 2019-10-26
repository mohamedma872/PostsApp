package com.sdody.postsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.constants.Constants
import com.sdody.postsapp.list.model.ListDataContract
import com.sdody.postsapp.list.viewmodel.ListViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Tests for [ListViewModel]
 * */
@RunWith(RobolectricTestRunner::class)
class ListViewModelTest {
    //mocking the needed classes
    private lateinit var viewModel: ListViewModel

    private val repo: ListDataContract.Repository = mock()

    private var postList: LiveData<PagedList<Post>> = mock()
    //private val factory: DataSource.Factory<Int, Post> = mock()


    @Before
    fun init() {

        viewModel = ListViewModel(repo, CompositeDisposable())
        viewModel.postList = postList

    }

    /*
        Mockito Verify methods are used to check that certain behavior happened. We can use Mockito verify methods at the end of the testing method code to make sure that specified methods are called.
    */
    @Test
    fun testGetPostsSuccess() {
        viewModel.fetchPosts()
        verify(repo).allPosts()
        verify(repo).getPostsFromRemote(0, 20)
    }


    @Test
    fun addPost() {
        val post = Post(11111, 12, "testtittle", "testbody", false)
        viewModel.addPost(post)
        verify(repo).addPost(post)
    }

    @Test
    fun editPost() {
        val post = Post(1, 1, "testtittle", "testbody", false)
        viewModel.updatePost(post)
        verify(repo).editPost(post)
    }

    @Test
    fun deletePost() {
        val post = Post(1, 1, "testtittle", "testbody", false)
        viewModel.deletePost(post)
        verify(repo).deletePost(post)
    }

}