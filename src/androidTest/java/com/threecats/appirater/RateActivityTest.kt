package com.threecats.appirater

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RateActivityTest {

    @Test
    fun testDisplayActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Appirater.init(context)

        val scenario = launchActivity<RateActivity>()
        scenario.onActivity {
            Thread.sleep(10000)
        }
    }
}