package com.sdody.postsapp.commons.data.local

import androidx.room.Entity

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Post(@SerializedName("userId") val userId: Int,
                @SerializedName("id") @PrimaryKey val postId: Long,
                @SerializedName("title") val postTitle: String,
                @SerializedName("body") val postBody: String, var issynced: Boolean)