package com.threecats.appirater.frontend

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.threecats.appirater.R
import com.threecats.appirater.RateActivity

class AndroidFrontend(val context: Context) : IFrontend {

    override fun askToRate() {
        Log.i(TAG, "Prompting to rate.")
        val intent = Intent(context, RateActivity::class.java)
        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun rateOnAppStore() {
        Log.i(TAG, "Launching app store to rate.")
        val playStoreIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(context.getString(R.string.appirater_market_url), context.packageName)))
        playStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Cannot launch app store! (not installed?)")
        }
    }

    companion object {
        private val TAG = AndroidFrontend::class.java.simpleName
    }
}
