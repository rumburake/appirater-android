package com.threecats.appirater

import com.threecats.appirater.FakeFrontend.FakeFrontend
import com.threecats.appirater.clock.FakeClock
import com.threecats.appirater.prefs.FakePrefs
import com.threecats.appirater.resources.FakeResources
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RaterTest {

    var fakeClock = FakeClock()
    var fakeFrontend = FakeFrontend()
    lateinit var fakePrefs: FakePrefs
    lateinit var fakeResources: FakeResources
    lateinit var rater: Rater

    @Before
    fun setup () {
        fakePrefs = FakePrefs(
                eventCount = 0,
                launchCount = 0,
                dateFirstLaunched = 0,
                dateDeferredRating = 0,
                doNotRate = false,
                alreadyRated = false,
                lastVersionCode = 0
        )
        fakeResources = FakeResources(
                testMode = false,
                minLaunches = 1,
                minEventsAfterTime = 3,
                minEventsAnytime = 5,
                minTimeFromLaunch = 1000,
                minTimeFromLater = 100
        )
        rater = Rater(
                fakeFrontend,
                fakeResources,
                fakePrefs,
                fakeClock
        )
    }

    private fun raterWithNewConfig(newConfig: FakeResources) {
        rater = Rater(fakeFrontend, newConfig, fakePrefs, fakeClock)
    }

    @Test
    fun `when events anytime hit, ask to rate`() {
        fakePrefs.eventCount = 4
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)

        rater.onEvent()
        rater.onLaunch()
        assertEquals(1, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `when events after time hit, ask to rate`() {
        fakePrefs.eventCount = 2
        fakeClock.time = 1000
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)

        rater.onEvent()
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)

        fakeClock.time = 2000
        rater.onLaunch()
        assertEquals(1, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `when event, then counted depending on test mode`() {
        rater.onEvent()
        assertEquals(1, fakePrefs.eventCount)

        raterWithNewConfig(fakeResources.copy(testMode = true))
        rater.onEvent()
        assertEquals(1, fakePrefs.eventCount)
    }

    @Test
    fun `given test mode, when launch, then asked to rate`() {
        rater.onEvent()
        assertEquals(0, fakeFrontend.timesAskedToRate)

        raterWithNewConfig(fakeResources.copy(testMode = true))
        rater.onLaunch()
        assertEquals(1, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `given already rated, when launch, then don't ask to rate`() {
        rater.clickedYes()
        assertEquals(1, fakeFrontend.timesRatedOnPlayStore)

        fakePrefs.eventCount = 10
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `given rating not wanted, when launch, then don't ask to rate`() {
        rater.clickedNo()
        assertEquals(0, fakeFrontend.timesRatedOnPlayStore)

        fakePrefs.eventCount = 10
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `given not enough launches, when launch, then don't ask to rate`() {
        raterWithNewConfig(fakeResources.copy(minLaunches = 2))

        fakePrefs.eventCount = 10
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `given deferred rating, when launch, then ask to rate only after time`() {
        fakePrefs.eventCount = 10

        fakeClock.time = 1000
        rater.clickedLater()
        assertEquals(0, fakeFrontend.timesRatedOnPlayStore)

        fakeClock.time = 1050
        rater.onLaunch()
        assertEquals(0, fakeFrontend.timesAskedToRate)

        fakeClock.time = 1150
        rater.onLaunch()
        assertEquals(1, fakeFrontend.timesAskedToRate)
    }

    @Test
    fun `direct rating command works`() {
        rater.rateOnAppStore()
        assertEquals(1, fakeFrontend.timesRatedOnPlayStore)
    }
}