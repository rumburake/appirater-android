package com.threecats.appirater.prefs

import android.content.Context
import android.content.SharedPreferences

class AndroidPrefs(context: Context): IPrefs {

    private val prefs: SharedPreferences = context.getSharedPreferences(context.packageName + ".appirater", 0)

    private fun getInt(key: String): Int {
        return prefs.getInt(key, 0)
    }

    private fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    private fun getLong(key: String): Long {
        return prefs.getLong(key, 0)
    }

    private fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    private fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    private fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    private val PREF_LAUNCH_COUNT = "launch_count"
    private val PREF_EVENT_COUNT = "event_count"
    private val PREF_ALREADY_RATED = "rate_clicked"
    private val PREF_DO_NOT_RATE = "dont_show"
    private val PREF_DATE_DEFERRED_RATING = "date_reminder_pressed"
    private val PREF_DATE_FIRST_LAUNCHED = "date_first_launched"
    private val PREF_LAST_VERSION_CODE = "version_code"

    override var eventCount: Int = getInt(PREF_EVENT_COUNT)
        set(value) {
            field = value
            putInt(PREF_EVENT_COUNT, value)
        }

    override var launchCount: Int = getInt(PREF_LAUNCH_COUNT)
        set(value) {
            field = value
            putInt(PREF_LAUNCH_COUNT, value)
        }

    override var dateFirstLaunched: Long = getLong(PREF_DATE_FIRST_LAUNCHED)
        set(value) {
            field = value
            putLong(PREF_DATE_FIRST_LAUNCHED, value)
        }

    override var doNotRate: Boolean = getBoolean(PREF_DO_NOT_RATE)
        set(value) {
            field = value
            putBoolean(PREF_DO_NOT_RATE, value)
        }

    override var alreadyRated: Boolean = getBoolean(PREF_ALREADY_RATED)
        set(value) {
            field = value
            putBoolean(PREF_ALREADY_RATED, value)
        }

    override var dateDeferredRating: Long = getLong(PREF_DATE_DEFERRED_RATING)
        set(value) {
            field = value
            putLong(PREF_DATE_DEFERRED_RATING, value)
        }

    override var lastVersionCode: Int = getInt(PREF_LAST_VERSION_CODE)
        set(value) {
            field = value
            putInt(PREF_LAST_VERSION_CODE, value)
        }
}
