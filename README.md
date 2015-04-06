Introduction
------------
This is a modified version of
   https://github.com/drewjw81/appirater-android/

It adds a few features:
  - all strings are overrideable via appirater-settings.xml instead of only the buttons
  - used a different layout so the title/divider color can be color matched to your app's theme
  - added callback for which buttons users press so you can do analytics on what they do
  - added support for timed/untimed major events in case you want to prompt for a rating only after a certain number of days
  - fixed missing language string so lint won't barf
  - added to Maven Central so it can be used w/ Android Studio

The class package name remains com.sbstrm.appirater for historical reasons, but the Maven artifact is in the com.keysolutions group because that's what I can upload to.


Maven Artifact
--------------
This library is in the Maven Central Library hosted by Sonatype.
In Gradle, you can reference it with this in your dependencies:

    compile group: 'com.keysolutions', name: 'appirater-android', version: '1.4.0.0'

And in Maven, you can reference it with this:
    <dependency>
      <groupId>com.keysolutions</groupId>
      <artifactId>appirater-android</artifactId>
      <version>1.4.0.0</version>
      <type>pom</type>
    </dependency>

Set up
-------------------------
1. Copy the /res/values/appirater-settings.xml from the AppiraterAndroid library in to your projects /res/values/ folder and adjust the settings to your preference.
2. Add Appirater.appLaunched(this); to the onCreate method in your main Activity.

appirater-settings.xml
-----------------------
These are all the settings that can be configured via the xml file:
 - appirater_app_title: your app name
 - appirater_rate_title: dialog title string
 - appirator_message: prompt for rating app
 - appirator_market_url: URL to app store to rate your app
 - appirator_days_until_prompt: days until it prompts you
 - appirator_launches_until_prompt: #launches until it prompts
 - appirator_untimed_events_until_prompt: #events until it prompts (no time limit)
 - appirator_timed_events_until_prompt: #events until it prompts but only after appirator_launches_until_prompt has been hit
 - appirator_button_rate: text for Rate Now button
 - appirator_button_rate_later: text for Later button
 - appirator_button_rate_cancel: text for Don't Rate button
 - appirator_days_before_reminding: #days before it reminds you after you press Later
 - appirator_test_mode: if set to "true", will show you dialog immediately for testing
 - appirator_button_start_color: gradient start color for buttons
 - appirator_button_end_color: gradient end color for buttons
 - appirator_button_text_color: button text color
 - appirator_title_color: title/divider color

Analytics Callback
------------------
There was a need for analytics callbacks for the buttons user clicked in the rating prompt dialog, so the RatingButtonListener interface was added.
You can add this support in the appLaunched() call as the second parameter; you'll get calls to the onRate, onLater, and onDeclined methods when users click them.  If you have no need for this, pass in null instead.
