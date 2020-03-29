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

    private enum class Keys {
        PREF_LAUNCH_COUNT,
        PREF_EVENT_COUNT,
        PREF_ALREADY_RATED,
        PREF_DO_NOT_RATE,
        PREF_DATE_DEFERRED_RATING,
        PREF_DATE_FIRST_LAUNCHED,
        PREF_LAST_VERSION_CODE
    }

    override var eventCount: Int = getInt(Keys.PREF_EVENT_COUNT.name)
        set(value) {
            field = value
            putInt(Keys.PREF_EVENT_COUNT.name, value)
        }

    override var launchCount: Int = getInt(Keys.PREF_LAUNCH_COUNT.name)
        set(value) {
            field = value
            putInt(Keys.PREF_LAUNCH_COUNT.name, value)
        }

    override var dateFirstLaunched: Long = getLong(Keys.PREF_DATE_FIRST_LAUNCHED.name)
        set(value) {
            field = value
            putLong(Keys.PREF_DATE_FIRST_LAUNCHED.name, value)
        }

    override var doNotRate: Boolean = getBoolean(Keys.PREF_DO_NOT_RATE.name)
        set(value) {
            field = value
            putBoolean(Keys.PREF_DO_NOT_RATE.name, value)
        }

    override var alreadyRated: Boolean = getBoolean(Keys.PREF_ALREADY_RATED.name)
        set(value) {
            field = value
            putBoolean(Keys.PREF_ALREADY_RATED.name, value)
        }

    override var dateDeferredRating: Long = getLong(Keys.PREF_DATE_DEFERRED_RATING.name)
        set(value) {
            field = value
            putLong(Keys.PREF_DATE_DEFERRED_RATING.name, value)
        }

    override var lastVersionCode: Int = getInt(Keys.PREF_LAST_VERSION_CODE.name)
        set(value) {
            field = value
            putInt(Keys.PREF_LAST_VERSION_CODE.name, value)
        }
}
