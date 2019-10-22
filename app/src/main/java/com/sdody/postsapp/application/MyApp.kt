package com.sdody.postsapp.application

import android.app.Application
import com.sdody.postsapp.commons.di.moduls.AppModule
import com.sdody.postsapp.commons.di.component.CoreComponent
import com.sdody.postsapp.commons.di.component.DaggerCoreComponent


open class MyApp : Application() {

    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        // Instead of instantiating SharedPreferences, Databases, Network Libraries in our Activity each time, we’ll prefer to have instances of them created somewhere else and inject them in our Activity when it’s needed.
       //instead of instantiating the SharedPreferences in our Activity every time, we’d let it be injected from another class.
        coreComponent = DaggerCoreComponent.builder().appModule(
            AppModule(
                this
            )
        ).build()
    }
}