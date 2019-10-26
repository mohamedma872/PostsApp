package com.sdody.postsapp

import com.sdody.postsapp.commons.testing.DependencyProvider
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.data.remote.PostService
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException


/**
 * Tests for [PostService]
 */
@RunWith(RobolectricTestRunner::class)
class PostServiceTest {
    //declare service and moc server
    private lateinit var postService: PostService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        postService = DependencyProvider
            .getRetrofit(mockWebServer.url("http://jsonplaceholder.typicode.com/"))
            .create(PostService::class.java)

    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

//    private fun queueResponse(block: MockResponse.() -> Unit) {
//        mockWebServer.enqueue(MockResponse().apply(block))
//    }

    //testing functions
    @Test
    fun fetchPosts()
    {


        postService
            .getPosts(0,20)
            .test()
            .run {
                assertNoErrors()
                assertValueCount(1)
                Assert.assertEquals(values()[0].size, 20)
                Assert.assertEquals(values()[0][0].postTitle, "sunt aut facere repellat " +
                        "provident occaecati excepturi optio reprehenderit")
                Assert.assertEquals(values()[0][0].userId, 1)
            }
    }

    @Test
    fun addPost()
    {
        val post = Post(1111,1,"testtittle","testbody",false,1)
        postService.addPost(post).test().run {
            assertNoErrors()

        }
    }

    @Test
    fun editPost()
    {
        val post = Post(1,1,"testtittle","testbody",false,2)
        postService.updatePost(post.postId!!,post).test().run {
            assertNoErrors()

        }
    }

    @Test
    fun deletePost()
    {
        postService.deletePost(1).test().run {

            assertNoErrors()
        }
    }
}


