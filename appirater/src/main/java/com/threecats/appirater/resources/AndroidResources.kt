package com.threecats.appirater.resources

import android.content.Context
import com.threecats.appirater.R
import com.threecats.appirater.Rater

class AndroidResources(context: Context): IResources {

    private val res = context.resources
    override val testMode = res.getBoolean(R.bool.appirater_test_mode)

    override val minLaunches = res.getInteger(R.integer.appirater_launches_until_prompt)
    override val minTimeFromLaunch = res.getInteger(R.integer.appirater_days_until_prompt).toLong() * Rater.DAY_IN_MILLIS
    override val minTimeFromLater = res.getInteger(R.integer.appirater_days_before_reminding).toLong() * Rater.DAY_IN_MILLIS
    override val minEventsAfterTime = res.getInteger(R.integer.appirater_timed_events_until_prompt)
    override val minEventsAnytime = res.getInteger(R.integer.appirater_untimed_events_until_prompt)
}
