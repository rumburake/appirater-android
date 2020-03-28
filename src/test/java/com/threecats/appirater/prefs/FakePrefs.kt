package com.threecats.appirater.prefs

class FakePrefs(
        override var eventCount: Long = 0,
        override var launchCount: Long = 0,
        override var dateFirstLaunch: Long = 0,
        override var dontRate: Boolean = false,
        override var doneRate: Boolean = false,
        override var dateRateLater: Long = 0,
        override var lastVersionCode: Int = 0
) : IPrefs
