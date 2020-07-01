package com.jumpitt.happ

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.orhanobut.hawk.Hawk
import io.realm.Realm
import io.realm.RealmConfiguration
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory

val context: Context by lazy { App.ctx!! }

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        initHawk()
        initRealm()
        Sentry.init("https://3a056092c4b243ee905d807cf8178cea@o205720.ingest.sentry.io/5275165", AndroidSentryClientFactory(this))
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        var ctx: Context? = null
            private set
    }

    private fun initHawk() {
        Hawk.init(this).build()
    }

    private fun initRealm(){
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(configuration)
    }
}