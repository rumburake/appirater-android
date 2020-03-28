package com.threecats.appirater.prefs

interface IPrefs {
    var eventCount: Int
    var launchCount: Int
    var dateFirstLaunch: Long
    var dontRate: Boolean
    var doneRate: Boolean
    var dateRateLater: Long
    var lastVersionCode: Int
}
