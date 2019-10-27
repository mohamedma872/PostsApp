package com.sdody.postsapp.commons.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var INSTANCE: PostDb

fun getDatabase(context: Context): PostDb {
    synchronized(PostDb::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    PostDb::class.java,
                    "chat_db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        CoroutineScope(Dispatchers.IO).launch {

                        }
                    }
                })
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}