package com.threecats.appirater;

import android.content.Context
import com.threecats.appirater.clock.SystemClock
import com.threecats.appirater.frontend.AndroidFrontend
import com.threecats.appirater.prefs.AndroidPrefs
import com.threecats.appirater.resources.AndroidResources

object Appirater {

    private var rater: Rater? = null

    @JvmStatic
    fun init(context: Context) {
        if (rater == null) {
            val appContext = context.applicationContext
            rater = Rater(
                    AndroidFrontend(appContext),
                    AndroidResources(appContext),
                    AndroidPrefs(appContext),
                    SystemClock()
            )
        }
    }

    @JvmStatic
    fun get(): Rater {
        rater?.let {
            return it
        }
        throw IllegalStateException("Appirater not initialized. Please call Appirater.init() with your Context before using.")
    }
}
