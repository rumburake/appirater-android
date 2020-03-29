package com.threecats.appirater

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RateActivityTest {

    @Test
    fun testRefuseToRate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Appirater.init(context)

        val scenario = launchActivity<RateActivity>()

        onView(withId(R.id.buttonNo))
                .perform(click())

        assertEquals(Lifecycle.State.DESTROYED, scenario.state)
    }

    @Test
    fun testDeferToRate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Appirater.init(context)

        val scenario = launchActivity<RateActivity>()

        onView(withId(R.id.buttonLater))
                .perform(click())

        assertEquals(Lifecycle.State.DESTROYED, scenario.state)
    }

}