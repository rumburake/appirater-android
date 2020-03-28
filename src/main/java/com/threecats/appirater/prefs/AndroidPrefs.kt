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
    private val PREF_RATE_CLICKED = "rate_clicked"
    private val PREF_DONT_SHOW = "dont_show"
    private val PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed"
    private val PREF_DATE_FIRST_LAUNCHED = "date_first_launched"
    private val PREF_APP_VERSION_CODE = "version_code"

    override var eventCount: Long = getLong(PREF_EVENT_COUNT)
        set(value) {
            field = value
            putLong(PREF_EVENT_COUNT, value)
        }

    override var launchCount: Long = getLong(PREF_LAUNCH_COUNT)
        set(value) {
            field = value
            putLong(PREF_LAUNCH_COUNT, value)
        }

    override var dateFirstLaunch: Long = getLong(PREF_DATE_FIRST_LAUNCHED)
        set(value) {
            field = value
            putLong(PREF_DATE_FIRST_LAUNCHED, value)
        }

    override var dontRate: Boolean = getBoolean(PREF_DONT_SHOW)
        set(value) {
            field = value
            putBoolean(PREF_DONT_SHOW, value)
        }

    override var doneRate: Boolean = getBoolean(PREF_RATE_CLICKED)
        set(value) {
            field = value
            putBoolean(PREF_RATE_CLICKED, value)
        }

    override var dateRateLater: Long = getLong(PREF_DATE_REMINDER_PRESSED)
        set(value) {
            field = value
            putLong(PREF_DATE_REMINDER_PRESSED, value)
        }

    override var lastVersionCode: Int = getInt(PREF_APP_VERSION_CODE)
        set(value) {
            field = value
            putInt(PREF_APP_VERSION_CODE, value)
        }
}
