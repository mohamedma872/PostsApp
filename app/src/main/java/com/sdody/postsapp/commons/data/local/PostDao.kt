package com.sdody.postsapp.commons.data.local

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Flowable


@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getPosts(): Flowable<List<Post>>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertall(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: Post)

    @Delete
    fun delete(post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post)

    @Query("SELECT * FROM post ORDER BY postId ASC")
    fun alPosts(): DataSource.Factory<Int, Post>
}