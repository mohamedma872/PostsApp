package com.sdody.postsapp.list.di.component

import com.sdody.postsapp.commons.data.local.PostDb
import com.sdody.postsapp.commons.data.remote.PostService
import com.sdody.postsapp.commons.di.component.CoreComponent
import com.sdody.postsapp.commons.networking.Scheduler
import com.sdody.postsapp.list.di.ListScope
import com.sdody.postsapp.list.di.module.ListModule
import com.sdody.postsapp.list.view.ListActivity
import dagger.Component

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    //Expose to dependent components
    fun postDb(): PostDb
    fun postService(): PostService
    fun scheduler(): Scheduler

    fun inject(listActivity: ListActivity)

}

