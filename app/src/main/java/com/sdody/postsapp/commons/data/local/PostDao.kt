package com.sdody.postsapp.commons.data.local

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Single


@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getPosts(): List<Post>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertall(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: Post)

    @Query("DELETE from post where postId = :id")
    fun delete(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post)

    @Query("SELECT * FROM post where opertaionType != 3  ORDER BY postId ASC")
    fun alPosts(): DataSource.Factory<Int, Post>


    @Query("SELECT * FROM  post where issynced = 0")
    fun getPostsNotSynced(): Single<List<Post>>
}