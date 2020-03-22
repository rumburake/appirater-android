Introduction
------------
A simple to integrate and easy to use application rater for Android apps.

How it works
------------
A dialog will prompt the user to rate the app on the app store. When the user:
 - chooses *Yes*, he is sent to the app store
 - chooses *Later*, he is reminded again later
 - chooses *No* he won't be bothered again
 
The dialog can look like this:

[Default Theme](AppiraterLight.png) [Customised Theme](AppiratorDark.png)

Set up
-------------------------

1. Check out the project in 

2. In app/settings.gradle add the library:

        dependencies {
            [...]
            compile project(':app:libs:appirater')
        }

3. Liberally modify your required Gradle, SDK and Support versions you want to use in your build in appirater/build.gradle

4. Copy the /res/values/appirater-settings.xml from the AppiraterAndroid library in to your projects /res/values/ folder and adjust the settings to your preference.
    At a minimum modify appirater_app_title to the title of your app.

5. Add Appirater.appLaunched(this); to the onCreate method in your main Activity.

        if (savedInstanceState == null) {
            Appirater.getRater(getApplicationContext()).appLaunched(this);
        }

6. Implement the RateDialog.RateDialogListener in your Activity as follow (ad literam):

        @Override
        public void rateAction(Appirater.RateDialogAction rateDialogAction) {
            Appirater.getRater(getApplicationContext()).rateAction(rateDialogAction);
        }

   This is because DialogFragment can only reliably talk back to an Activity what created it.
   The glue above delivers the action from whichever Activity was Dialog's parent to the AppiRater logic.
   This is useful when rotate screens or leaving and returning to the app occurred while dialog was displayed.
   The reason for this ugly hook is because new Dialog as Fragment APIs encumber any libraries in providing seamless ready to use Dialogs. See:
        http://developer.android.com/guide/topics/ui/dialogs.html#PassingEvents

appirater-settings.xml
-----------------------
These are all the settings that can be configured via the xml file:
 - appirater_app_title: your app name
 - appirater_rate_title: dialog title string
 - appirater_message: prompt for rating app
 - appirater_market_url: URL to app store to rate your app
 - appirater_days_until_prompt: days until it prompts you
 - appirater_launches_until_prompt: #launches until it prompts
 - appirater_untimed_events_until_prompt: #events until it prompts (no time limit)
 - appirater_timed_events_until_prompt: #events until it prompts but only after appirater_launches_until_prompt has been hit
 - appirater_button_rate: text for Rate Now button
 - appirater_button_rate_later: text for Later button
 - appirater_button_rate_cancel: text for Don't Rate button
 - appirater_days_before_reminding: #days before it reminds you after you press Later
 - appirater_test_mode: if set to "true", will show you dialog immediately for testing
 - appirater_button_start_color: gradient start color for buttons
 - appirater_button_end_color: gradient end color for buttons
 - appirater_button_text_color: button text color
 - appirater_title_color: title/divider color

License
-------------------------
MIT/X11: http://opensource.org/licenses/MIT

This is originally forked from:
    https://github.com/kenyee/appirater-android
...which is a modified version of:
    https://github.com/drewjw81/appirater-android/

