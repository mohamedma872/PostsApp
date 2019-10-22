package com.sdody.postsapp.commons.data.remote


import com.sdody.postsapp.commons.data.local.Post
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.*

interface PostService {
    @GET("/posts/")
    fun getPosts(): Single<List<Post>>

    @POST("/posts")
    fun addPost(@Body post: Post): Completable

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int) : Completable

    @PUT("posts/{id}")
    fun updatePost(@Path("id")id: Int, @Body post: Post) : Completable

}