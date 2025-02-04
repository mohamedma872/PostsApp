package com.sdody.postsapp.list.di


import com.sdody.postsapp.application.BaseApp
import com.sdody.postsapp.list.di.component.DaggerListComponent
import com.sdody.postsapp.list.di.component.ListComponent
import javax.inject.Singleton

@Singleton
object PostDH {
    private var listComponent: ListComponent? = null

    fun listComponent(): ListComponent {
        if (listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(
                BaseApp
                .coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

}