package com.threecats.appirater.FakeFrontend

import com.threecats.appirater.frontend.IFrontend

class FakeFrontend : IFrontend {

    var timesAskedToRate = 0

    var timesRatedOnPlayStore = 0

    override fun askToRate() {
        timesAskedToRate++
    }

    override fun rateOnAppStore() {
        timesRatedOnPlayStore++
    }
}
