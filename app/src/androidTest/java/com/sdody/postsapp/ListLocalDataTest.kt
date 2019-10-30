package com.sdody.postsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sdody.postsapp.commons.data.local.PostDb
import com.sdody.postsapp.commons.testing.DummyData
import com.sdody.postsapp.commons.testing.TestScheduler
import com.sdody.postsapp.list.model.ListLocalData
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 *
 * Test for [ListLocalData]
 * Needs to be an instrumented test because Room needs to be tested on a physical device:
 * https://developer.android.com/training/data-storage/room/testing-db.html#android
 *
 * */
@RunWith(AndroidJUnit4::class)
class ListLocalDataTest {

    private lateinit var postDb: PostDb

    private val listLocalData: ListLocalData by lazy { ListLocalData(postDb,
        TestScheduler()
    ) }

    //Necessary for Room insertions to work
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dummyPosts = listOf(DummyData.post(1, 1), DummyData.post(2, 2))

    @Before
    fun init() {
        postDb = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, PostDb::class.java)
                .allowMainThreadQueries()
                .build()
    }

    /**
     * Test that [ListLocalData.getPosts] fetches the posts and users in the database
    * */
    @Test
    fun testGetPosts() {

        postDb.postDao().insertall(dummyPosts)
        assertThat(listLocalData.getPosts()[0], equalTo(dummyPosts[0]))
    }


    /**
     * Test that [ListLocalData.savedPosts] saves the passed lists into the database
     * */
    @Test
    fun savePosts() {

        listLocalData.savedPosts( dummyPosts)
        val posts = postDb.postDao().getPosts()
        assertThat(posts[0], equalTo(dummyPosts[0]))
    }
    @Test
    fun deletePost() {
        //save posts in db
        listLocalData.savedPosts( dummyPosts)
        //get posts from db
        val posts = postDb.postDao().getPosts()
       //make sure that posts has been insertted in db
        assertThat(posts[0], equalTo(dummyPosts[0]))
        //delete post from db
        listLocalData.deletePost(posts[0])
        //get posts from db after delete post from db
        val postsafterdeletion = postDb.postDao().getPosts()
        //the posts[0] should not equal to posts_afterdeletion.get(0)
        assertNotEquals(posts[0], equalTo(postsafterdeletion[0]))
        //the size of the posts array should be greater than posts_afterdeletion.size
        assertEquals(posts.size-1,postsafterdeletion.size)


    }
    @After
    @Throws(IOException::class)
    fun clean() {
        postDb.close()
    }
}