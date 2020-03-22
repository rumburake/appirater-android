package com.threecats.appirater

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.lang.String.format

class Appirater(val context: Context) {
    private val PREF_LAUNCH_COUNT = "launch_count"
    private val PREF_EVENT_COUNT = "event_count"
    private val PREF_RATE_CLICKED = "rate_clicked"
    private val PREF_DONT_SHOW = "dont_show"
    private val PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed"
    private val PREF_DATE_FIRST_LAUNCHED = "date_first_launched"
    private val PREF_APP_VERSION_CODE = "version_code"

    private val res = context.resources
    private val prefs = context.getSharedPreferences(context.packageName + ".appirater", 0)
    private val editor = prefs.edit()

    private val testMode = res.getBoolean(R.bool.appirater_test_mode)

    private val minLaunches = res.getInteger(R.integer.appirater_launches_until_prompt)
    private val minTimeFromLaunch = res.getInteger(R.integer.appirater_days_until_prompt).toLong() * DAY_IN_MILLIS
    private val minTimeFromLater = res.getInteger(R.integer.appirater_days_before_reminding).toLong() * DAY_IN_MILLIS
    private val minEventsAfterTime = res.getInteger(R.integer.appirater_timed_events_until_prompt)
    private val minEventsAnytime = res.getInteger(R.integer.appirater_untimed_events_until_prompt)

    private var eventCount: Long = prefs.getLong(PREF_EVENT_COUNT, 0)
    private var launchCount: Long = prefs.getLong(PREF_LAUNCH_COUNT, 0)
    private var dateFirstLaunch: Long = prefs.getLong(PREF_DATE_FIRST_LAUNCHED, 0)
    private var dontRate: Boolean = prefs.getBoolean(PREF_DONT_SHOW, false)
    private var doneRate: Boolean = prefs.getBoolean(PREF_RATE_CLICKED, false)
    private var dateRateLater: Long = prefs.getLong(PREF_DATE_REMINDER_PRESSED, 0)

    init {
        val lastVersionCode = prefs.getInt(PREF_APP_VERSION_CODE, 0)
        if (BuildConfig.VERSION_CODE != lastVersionCode) {
            // user should have enough meaningful events
            //  i.e. used enough the current version before invited to rate
            editor.putLong(PREF_EVENT_COUNT, 0)
            editor.putInt(PREF_APP_VERSION_CODE, BuildConfig.VERSION_CODE)
            editor.apply()
        }
    }

    fun rateOnAppStore() {
        val playStoreIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(format(context.getString(R.string.appirater_market_url), context.packageName)))
        playStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Cannot launch app store! (not installed?)")
        }
    }

    fun onEvent() {
        if (testMode) {
            // test mode, don't record event
            return
        }
        eventCount++
        editor.putLong(PREF_EVENT_COUNT, eventCount).apply()
    }

    fun onLaunch() {
        if (testMode) {
            // test mode, show dialog directly
            askToRate()
            return
        }
        // increment launch count
        launchCount++
        editor.putLong(PREF_LAUNCH_COUNT, launchCount)
        // record date if this is the first launch
        if (dateFirstLaunch == 0L) {
            dateFirstLaunch = System.currentTimeMillis()
            editor.putLong(PREF_DATE_FIRST_LAUNCHED, dateFirstLaunch)
        }
        editor.apply()

        ifUsedEnoughShowDialog()
    }

    private fun ifUsedEnoughShowDialog() {
        if (doneRate) {
            // user already rated (i.e. was sent to app store)
            return
        }
        if (dontRate) {
            // user doesn't want to rate
            return
        }
        if (launchCount < minLaunches) {
            // a minimum number of launches required before showing the dialog at all
            return
        }
        if (dateRateLater > 0 && System.currentTimeMillis() < dateRateLater + minTimeFromLater) {
            // user delayed rating, don't ask until delay is passed
            return
        }
        if (minEventsAnytime in 1..eventCount) {
            // if a lot of events happened, any time, ask to rate
            askToRate()
            return
        }
        if (eventCount >= minEventsAfterTime && System.currentTimeMillis() >= dateFirstLaunch + minTimeFromLaunch) {
            // if some events happened but they were over a minimum period of time, show dialog
            askToRate()
            return
        }
    }

    private fun askToRate() {
        val intent = Intent(context, RateActivity::class.java)
        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    internal fun clickedYes() {
        doneRate = true
        editor.putBoolean(PREF_RATE_CLICKED, doneRate).apply()
        rateOnAppStore()
    }

    internal fun clickedLater() {
        dateRateLater = System.currentTimeMillis()
        editor.putLong(PREF_DATE_REMINDER_PRESSED, dateRateLater).apply()
    }

    internal fun clickedNo() {
        dontRate = true
        editor.putBoolean(PREF_DONT_SHOW, dontRate).apply()
    }

    companion object {
        private val TAG = Appirater::class.java.simpleName
        private const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000L

        private var appirater: Appirater? = null

        fun init(context: Context) {
            if (appirater == null) {
                appirater = Appirater(context.applicationContext)
            }
        }

        fun get(): Appirater {
            appirater?.let {
                return it
            }
            throw IllegalStateException("Appirater not initialized. Please call Appirater.init() from your Context before using.")
        }
    }
}