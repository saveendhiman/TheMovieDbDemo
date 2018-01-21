package com.themoviedbdemo.base

import android.app.Application
import com.themoviedbdemo.api.NetModule
import com.themoviedbdemo.utils.UtilsModule
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import com.themoviedbdemo.BuildConfig

/**
 * Created by Saveen on 21/01/18.
 * Initialization of required libraries
 * MultiDexApplication
 */
class TheMovieDBApplication : Application() {

    lateinit var appComponent: AppComponent private set

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())

        //create component
        appComponent = DaggerAppComponent.builder()
                .utilsModule(UtilsModule(this)).netModule(NetModule(this)).build()

        appComponent.inject(this)

        //configure timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
