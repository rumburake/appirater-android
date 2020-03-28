package com.threecats.appirater.prefs

interface IPrefs {
    var eventCount: Long
    var launchCount: Long
    var dateFirstLaunch: Long
    var dontRate: Boolean
    var doneRate: Boolean
    var dateRateLater: Long
    var lastVersionCode: Int
}
