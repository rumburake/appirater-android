package com.threecats.appirater.prefs

interface IPrefs {
    var eventCount: Int
    var launchCount: Int
    var dateFirstLaunched: Long
    var doNotRate: Boolean
    var alreadyRated: Boolean
    var dateDeferredRating: Long
    var lastVersionCode: Int
}
