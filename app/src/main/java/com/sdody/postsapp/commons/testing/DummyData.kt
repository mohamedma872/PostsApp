package com.sdody.postsapp.commons.testing

import androidx.annotation.VisibleForTesting

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
object DummyData {
    fun Post(userId: Long, id: Long) = com.sdody.postsapp.commons.data.local.Post(userId, id, "title$id", "body$id",false)
}