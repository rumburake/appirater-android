package com.threecats.appirater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rate.*

class RateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val appName: String = getString(R.string.appirater_app_title)
        textTitle.text = getString(R.string.appirater_rate_title, appName)
        textMessage.text = getString(R.string.appirater_message, appName)
        buttonYes.text = getString(R.string.appirater_button_rate, appName)
        buttonLater.text = getString(R.string.appirater_button_rate_later)
        buttonNo.text = getString(R.string.appirater_button_rate_cancel)

        val appirater = Appirater.get()

        buttonYes.setOnClickListener {
            appirater.clickedYes()
            finish()
        }
        buttonLater.setOnClickListener {
            appirater.clickedLater()
            finish()
        }
        buttonNo.setOnClickListener {
            appirater.clickedNo()
            finish()
        }
    }
}
