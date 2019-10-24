package com.sdody.postsapp.commons.di.moduls


import android.content.Context
import com.sdody.postsapp.commons.networking.AppScheduler
import com.sdody.postsapp.commons.networking.Scheduler

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {
    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun providescheduler(): Scheduler {
        return AppScheduler()
    }
}