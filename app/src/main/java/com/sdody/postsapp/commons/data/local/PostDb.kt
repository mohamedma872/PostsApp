package com.sdody.postsapp.commons.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 3,exportSchema = false)
abstract class PostDb : RoomDatabase() {
    abstract fun postDao(): PostDao

}
