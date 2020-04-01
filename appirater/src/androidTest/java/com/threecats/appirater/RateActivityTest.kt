package com.threecats.appirater

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
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

    @get:Rule
    var intentsTestRule: IntentsTestRule<RateActivity> = IntentsTestRule(RateActivity::class.java, false, false)

    @Test
    fun testAcceptToRate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Appirater.init(context)

        intentsTestRule.launchActivity(Intent())

        onView(withId(R.id.buttonYes))
                .perform(click())

        intended(hasData(Uri.parse(String.format(context.getString(R.string.appirater_market_url), context.packageName))))
    }
}
