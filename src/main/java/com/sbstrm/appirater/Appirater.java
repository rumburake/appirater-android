package com.sbstrm.appirater;

/*	
 * @source https://github.com/sbstrm/appirater-android
 * @license MIT/X11
 * 
 * Copyright (c) 2011-2013 sbstrm Y.K.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Appirater {

    public enum RateDialogAction {
        RATE_YES,
        RATE_LATER,
        RATE_NO
    }

    private static final String PREF_LAUNCH_COUNT = "launch_count";
    private static final String PREF_EVENT_COUNT = "event_count";
    private static final String PREF_RATE_CLICKED = "rateclicked";
    private static final String PREF_DONT_SHOW = "dontshow";
    private static final String PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed";
    private static final String PREF_DATE_FIRST_LAUNCHED = "date_firstlaunch";
    private static final String PREF_APP_VERSION_CODE = "versioncode";

    private static Appirater sAppirater;

    public static Appirater getRater(Context context) {
        if (sAppirater == null) {
            sAppirater = new Appirater(context);
        }
        return sAppirater;
    }

    Appirater(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(context.getPackageName() + ".appirater", 0);
        mEditor = mPrefs.edit();
    }

    Context mContext;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public void reset() {
        mEditor.clear().commit();
    }

    public void appLaunched(FragmentActivity activity) {
        boolean testMode = mContext.getResources().getBoolean(R.bool.appirator_test_mode);
        if (!testMode && (mPrefs.getBoolean(PREF_DONT_SHOW, false) || mPrefs.getBoolean(PREF_RATE_CLICKED, false))) {
            return;
        }

        mEditor = mPrefs.edit();

        if (testMode) {
            showRateDialog(activity);
            return;
        }

        // Increment launch counter
        long launch_count = mPrefs.getLong(PREF_LAUNCH_COUNT, 0);

        // Get events counter
        long event_count = mPrefs.getLong(PREF_EVENT_COUNT, 0);

        // Get date of first launch
        long date_firstLaunch = mPrefs.getLong(PREF_DATE_FIRST_LAUNCHED, 0);

        // Get reminder date pressed
        long date_reminder_pressed = mPrefs.getLong(PREF_DATE_REMINDER_PRESSED, 0);

        try {
            int appVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            if (mPrefs.getInt(PREF_APP_VERSION_CODE, 0) != appVersionCode) {
                //Reset the launch and event counters to help assure users are rating based on the latest version.
                launch_count = 0;
                event_count = 0;
                mEditor.putLong(PREF_EVENT_COUNT, event_count);
            }
            mEditor.putInt(PREF_APP_VERSION_CODE, appVersionCode);
        } catch (Exception e) {
            //do nothing
        }

        launch_count++;
        mEditor.putLong(PREF_LAUNCH_COUNT, launch_count);

        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            mEditor.putLong(PREF_DATE_FIRST_LAUNCHED, date_firstLaunch);
        }

        // Wait at least N days and/or M events before opening
        // timed events will wait until N days have gone by
        // untimed events will not care about N days
        if (launch_count >= mContext.getResources().getInteger(R.integer.appirator_launches_until_prompt)) {
            long millisecondsToWait = mContext.getResources().getInteger(R.integer.appirator_days_until_prompt) * 24 * 60 * 60 * 1000L;
            int untimedEvents = mContext.getResources().getInteger(R.integer.appirator_untimed_events_until_prompt);
            if (System.currentTimeMillis() >= (date_firstLaunch + millisecondsToWait)
                    || ((untimedEvents > 0) && event_count >= untimedEvents)) {
                int timedEvents = mContext.getResources().getInteger(R.integer.appirator_timed_events_until_prompt);
                if ((timedEvents == 0) || (event_count >= timedEvents)) {
                    if (date_reminder_pressed == 0) {
                        showRateDialog(activity);
                    } else {
                        long remindMillisecondsToWait = mContext.getResources().getInteger(R.integer.appirator_days_before_reminding) * 24 * 60 * 60 * 1000L;
                        if (System.currentTimeMillis() >= (remindMillisecondsToWait + date_reminder_pressed)) {
                            showRateDialog(activity);
                        }
                    }
                }
            }
        }

        mEditor.commit();
    }

    public void significantEvent() {
        boolean testMode = mContext.getResources().getBoolean(R.bool.appirator_test_mode);
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".appirater", 0);
        if (!testMode && (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs.getBoolean(PREF_RATE_CLICKED, false))) {
            return;
        }
        long event_count = prefs.getLong(PREF_EVENT_COUNT, 0);
        event_count++;
        prefs.edit().putLong(PREF_EVENT_COUNT, event_count).commit();
    }

    private void showRateDialog(FragmentActivity activity) {
        RateDialog rateDialog = new RateDialog();
        Bundle args = new Bundle();
        String appName = mContext.getString(R.string.appirator_app_title);
        args.putString("title", String.format(mContext.getString(R.string.appirator_rate_title), appName));
        args.putString("message", String.format(mContext.getString(R.string.appirator_message), appName));
        args.putString("yes", String.format(mContext.getString(R.string.appirator_button_rate), appName));
        args.putString("later", mContext.getString(R.string.appirator_button_rate_later));
        args.putString("no", mContext.getString(R.string.appirator_button_rate_cancel));
        rateDialog.setArguments(args);
        rateDialog.show(activity.getSupportFragmentManager(), "appirater");
    }

    public void rateDirect() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(mContext.getString(R.string.appirator_market_url), mContext.getPackageName())));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(i);
        } catch (ActivityNotFoundException e) {
            // no market
        }
    }

    public void rateAction(RateDialogAction rateDialogAction) {
        switch (rateDialogAction) {
            case RATE_YES:
                rateDirect();
                mEditor.putBoolean(PREF_RATE_CLICKED, true);
                mEditor.commit();
                break;
            case RATE_LATER:
                mEditor.putLong(Appirater.PREF_DATE_REMINDER_PRESSED, System.currentTimeMillis());
                mEditor.commit();
                break;
            case RATE_NO:
                mEditor.putBoolean(Appirater.PREF_DONT_SHOW, true);
                mEditor.commit();
                break;
        }
    }
}