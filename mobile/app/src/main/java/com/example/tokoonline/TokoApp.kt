package com.example.tokoonline

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TokoApp: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else
            Timber.plant(CrashReportingTree())
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        private val crashlytics get() = FirebaseCrashlytics.getInstance()

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority < Log.INFO)
                return

            // non-fatal exception
            t?.let(crashlytics::recordException)
            crashlytics.log("$tag - $message")
        }
    }
}