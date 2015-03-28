package com.sbstrm.appirater;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Appirater {

    private static final String PREF_LAUNCH_COUNT = "launch_count";
    private static final String PREF_EVENT_COUNT = "event_count";
    private static final String PREF_RATE_CLICKED = "rateclicked";
    private static final String PREF_DONT_SHOW = "dontshow";
    private static final String PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed";
    private static final String PREF_DATE_FIRST_LAUNCHED = "date_firstlaunch";
    private static final String PREF_APP_VERSION_CODE = "versioncode";

    public static void appLaunched(Context mContext, RatingButtonListener ratingButtonListener) {
        boolean testMode = mContext.getResources().getBoolean(R.bool.appirator_test_mode);
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".appirater", 0);
        if (!testMode && (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs.getBoolean(PREF_RATE_CLICKED, false))) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        if (testMode) {
            showRateDialog(mContext, editor, ratingButtonListener);
            return;
        }

        // Increment launch counter
        long launch_count = prefs.getLong(PREF_LAUNCH_COUNT, 0);

        // Get events counter
        long event_count = prefs.getLong(PREF_EVENT_COUNT, 0);

        // Get date of first launch
        long date_firstLaunch = prefs.getLong(PREF_DATE_FIRST_LAUNCHED, 0);

        // Get reminder date pressed
        long date_reminder_pressed = prefs.getLong(PREF_DATE_REMINDER_PRESSED, 0);

        try {
            int appVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            if (prefs.getInt(PREF_APP_VERSION_CODE, 0) != appVersionCode) {
                //Reset the launch and event counters to help assure users are rating based on the latest version.
                launch_count = 0;
                event_count = 0;
                editor.putLong(PREF_EVENT_COUNT, event_count);
            }
            editor.putInt(PREF_APP_VERSION_CODE, appVersionCode);
        } catch (Exception e) {
            //do nothing
        }

        launch_count++;
        editor.putLong(PREF_LAUNCH_COUNT, launch_count);

        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(PREF_DATE_FIRST_LAUNCHED, date_firstLaunch);
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
                        showRateDialog(mContext, editor, ratingButtonListener);
                    } else {
                        long remindMillisecondsToWait = mContext.getResources().getInteger(R.integer.appirator_days_before_reminding) * 24 * 60 * 60 * 1000L;
                        if (System.currentTimeMillis() >= (remindMillisecondsToWait + date_reminder_pressed)) {
                            showRateDialog(mContext, editor, ratingButtonListener);
                        }
                    }
                }
            }
        }

        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void rateApp(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".appirater", 0);
        SharedPreferences.Editor editor = prefs.edit();
        rateApp(mContext, editor);
    }

    public static void significantEvent(Context mContext) {
        boolean testMode = mContext.getResources().getBoolean(R.bool.appirator_test_mode);
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".appirater", 0);
        if (!testMode && (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs.getBoolean(PREF_RATE_CLICKED, false))) {
            return;
        }

        long event_count = prefs.getLong(PREF_EVENT_COUNT, 0);
        event_count++;
        prefs.edit().putLong(PREF_EVENT_COUNT, event_count).apply();
    }

    private static void rateApp(Context mContext, final SharedPreferences.Editor editor) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(mContext.getString(R.string.appirator_market_url), mContext.getPackageName()))));
        if (editor != null) {
            editor.putBoolean(PREF_RATE_CLICKED, true);
            editor.commit();
        }
    }

    @SuppressLint("NewApi")
    private static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor,
                                       final RatingButtonListener ratingButtonListener) {
        String appName = mContext.getString(R.string.appirator_app_title);
        final Dialog dialog = new Dialog(mContext);

        // no title because we can't control the color...title is now in the layout
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        @SuppressLint("InflateParams")
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.appirater, null);

        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText(String.format(mContext.getString(R.string.appirator_rate_title), appName));

        TextView tv = (TextView) layout.findViewById(R.id.message);
        tv.setText(String.format(mContext.getString(R.string.appirator_message), appName));

        Button rateButton = (Button) layout.findViewById(R.id.rate);
        rateButton.setText(String.format(mContext.getString(R.string.appirator_button_rate), appName));
        rateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rateApp(mContext, editor);
                dialog.dismiss();
                if (ratingButtonListener != null) {
                    ratingButtonListener.onRate();
                }
            }
        });

        Button rateLaterButton = (Button) layout.findViewById(R.id.rateLater);
        rateLaterButton.setText(mContext.getString(R.string.appirator_button_rate_later));
        rateLaterButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putLong(PREF_DATE_REMINDER_PRESSED, System.currentTimeMillis());
                    editor.commit();
                }
                dialog.dismiss();
                if (ratingButtonListener != null) {
                    ratingButtonListener.onLater();
                }
            }
        });

        Button cancelButton = (Button) layout.findViewById(R.id.cancel);
        cancelButton.setText(mContext.getString(R.string.appirator_button_rate_cancel));
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(PREF_DONT_SHOW, true);
                    editor.commit();
                }
                dialog.dismiss();
                if (ratingButtonListener != null) {
                    ratingButtonListener.onDeclined();
                }
            }
        });

        dialog.setContentView(layout);
        dialog.show();
    }

    /**
     * Listener for finding out whether a button has been pressed by the user
     */
    public static abstract class RatingButtonListener {
        /**
         * Called if the user clicks on the Rate button to go to the Play store
         */
        public abstract void onRate();

        /**
         * Called if the user clicks on the Later button to remind them later
         */
        public abstract void onLater();

        /**
         * Called if the user clicks on the No/Cancel button so they're no longer prompted
         */
        public abstract void onDeclined();
    }
}