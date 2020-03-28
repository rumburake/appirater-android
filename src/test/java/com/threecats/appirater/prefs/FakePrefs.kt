package com.threecats.appirater.prefs

class FakePrefs(
        override var eventCount: Int = 0,
        override var launchCount: Int = 0,
        override var dateFirstLaunched: Long = 0,
        override var doNotRate: Boolean = false,
        override var alreadyRated: Boolean = false,
        override var dateDeferredRating: Long = 0,
        override var lastVersionCode: Int = 0
) : IPrefs
