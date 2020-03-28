package com.threecats.appirater

import com.threecats.appirater.clock.IClock
import com.threecats.appirater.frontend.AndroidFrontend
import com.threecats.appirater.prefs.IPrefs
import com.threecats.appirater.resources.IResources

class Rater(
        private val frontend: AndroidFrontend,
        private val res: IResources,
        private val prefs: IPrefs,
        private val clock: IClock
) {
    init {
        if (BuildConfig.VERSION_CODE != prefs.lastVersionCode) {
            // user should have enough meaningful events
            //  i.e. used enough the current version before invited to rate
            prefs.eventCount = 0
            prefs.lastVersionCode = BuildConfig.VERSION_CODE
        }
    }

    fun rateOnAppStore() {
        frontend.rateOnAppStore()
    }

    fun onEvent() {
        if (res.testMode) {
            // test mode, don't record event
            return
        }
        prefs.eventCount++
    }

    fun onLaunch() {
        if (res.testMode) {
            // test mode, show dialog directly
            frontend.askToRate()
            return
        }
        // increment launch count
        prefs.launchCount++
        // record date if this is the first launch
        if (prefs.dateFirstLaunched == 0L) {
            prefs.dateFirstLaunched = clock.currentTimeMillis()
        }

        ifUsedEnoughShowDialog()
    }

    private fun ifUsedEnoughShowDialog() {
        if (prefs.alreadyRated) {
            // user already rated (i.e. was sent to app store)
            return
        }
        if (prefs.doNotRate) {
            // user doesn't want to rate
            return
        }
        if (prefs.launchCount < res.minLaunches) {
            // a minimum number of launches required before showing the dialog at all
            return
        }
        if (prefs.dateDeferredRating > 0 && clock.currentTimeMillis() < prefs.dateDeferredRating + res.minTimeFromLater) {
            // user delayed rating, don't ask until delay is passed
            return
        }
        if (res.minEventsAnytime in 1..prefs.eventCount) {
            // if a lot of events happened, any time, ask to rate
            frontend.askToRate()
            return
        }
        if (prefs.eventCount >= res.minEventsAfterTime && clock.currentTimeMillis() >= prefs.dateFirstLaunched + res.minTimeFromLaunch) {
            // if some events happened but they were over a minimum period of time, show dialog
            frontend.askToRate()
            return
        }
    }

    internal fun clickedYes() {
        prefs.alreadyRated = true
        frontend.rateOnAppStore()
    }

    internal fun clickedLater() {
        prefs.dateDeferredRating = clock.currentTimeMillis()
    }

    internal fun clickedNo() {
        prefs.doNotRate = true
    }

    companion object {
        private val TAG = Rater::class.java.simpleName
        internal const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
    }
}