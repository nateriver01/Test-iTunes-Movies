package id.hmd.itunesmovies

import android.app.Application
import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class iTunesMoviesApp : MultiDexApplication() {
    companion object {
        lateinit var instance: Application
        var isOfflineShowing: Boolean = false
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SQLiteDatabase.loadLibs(instance)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}