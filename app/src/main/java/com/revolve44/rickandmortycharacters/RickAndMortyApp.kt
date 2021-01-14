package com.revolve44.rickandmortycharacters

import android.app.Application
import timber.log.Timber

class RickAndMortyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("ars")
        }
    }
}