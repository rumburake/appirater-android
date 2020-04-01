package com.threecats.appirater.clock

class SystemClock: IClock {

    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}