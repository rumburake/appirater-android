package com.threecats.appirater.resources

interface IResources {

    val testMode: Boolean

    val minLaunches: Int
    val minTimeFromLaunch: Long
    val minTimeFromLater: Long
    val minEventsAfterTime: Int
    val minEventsAnytime: Int
}
