package com.threecats.appirater.resources

class FakeResources(
        override val testMode: Boolean,
        override val minLaunches: Int,
        override val minTimeFromLaunch: Long,
        override val minTimeFromLater: Long,
        override val minEventsAfterTime: Int,
        override val minEventsAnytime: Int
) : IResources
