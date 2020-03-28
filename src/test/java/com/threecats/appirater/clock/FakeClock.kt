package com.threecats.appirater.clock

class FakeClock: IClock {

    var time = 0L

    override fun currentTimeMillis(): Long {
        return time
    }
}
