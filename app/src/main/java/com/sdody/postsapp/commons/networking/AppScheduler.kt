package com.sdody.postsapp.commons.networking

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [Scheduler] with actual threads.
 * */
class AppScheduler : Scheduler {

    override fun mainThread(): io.reactivex.Scheduler {
        return AndroidSchedulers.mainThread()
    }
//    IO — This is one of the most common types of Schedulers that are used. They are generally used for IO related stuff. Such as network requests, file system operations
    override fun io(): io.reactivex.Scheduler {
        return Schedulers.io()
    }
}