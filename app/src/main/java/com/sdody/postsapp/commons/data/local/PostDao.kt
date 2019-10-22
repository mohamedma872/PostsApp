package com.sdody.postsapp.commons.data.local

import androidx.room.*
import io.reactivex.Flowable


@Dao
interface PostDao {
    @Query("SELECT userId, post.postId AS postId, post.postTitle AS postTitle ,post.postBody AS postBody FROM post")
    fun getPosts(): Flowable<List<Post>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertall(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: Post)

    @Delete
    fun delete(post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post)

    @Query("SELECT * FROM post")
    fun getAll(): Flowable<List<Post>>
}