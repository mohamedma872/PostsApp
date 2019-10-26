package com.sdody.postsapp.commons.data.local

import androidx.room.Entity

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// operation type 1 for insert new post
// operation type 2 for update new post
// operation type 3 for delete new post
// operation type 0 for default
@Entity
data class Post(@SerializedName("userId") val userId: Long,
                @SerializedName("id") @PrimaryKey val postId: Long? = null,
                @SerializedName("title") val postTitle: String,
                @SerializedName("body") val postBody: String, var issynced: Boolean,var opertaionType: Int = 0)