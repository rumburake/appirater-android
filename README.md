# Introduction

A simple to integrate and easy to use application rater for Android apps.

# How it works

You want users to rate your app on the play store. They are unlikely to do it even if they want to, 
so a prompt will remind them to rate and take them straight to the app's store page where they can 
rate it with the least effort.

## User Interface

A dialog will prompt the user to rate the app on the app store. When the user:
 - chooses *Yes*, he is sent to the app's page on the app store
 - chooses *Later*, he will be reminded to rate the app at a later time
 - chooses *No* he doesn't want to rate at all so he won't be bothered again
 
### How it looks like

| Default Theme                                | Customised Theme                            |
|----------------------------------------------|---------------------------------------------|
| <img src="./AppiraterLight.png" width="300"> | <img src="./AppiratorDark.png" width="300"> |

## Logic

You should not prompt the users straight when they open the app to rate it, as this might annoy 
them and it could backfire in uninstall or negative rating. Also the users aren't ready to give a 
fair rating unless they had a chance to use the app.

Imagine you made a web browser. To maximise the chances or a fair rating, we should define some 
rules of when the user will be invited to rate. For example:
 - don't prompt until the app was opened at least 10 times 
 - don't prompt in the first 3 days, and also until the user loaded at least 15 web pages
 - however if the user used the app intensively and loaded more than 30 pages, prompt can appear before 3 days
 - if the user was prompted before and he either went to the app store or he refused, don't bother again
 - if the user said he was going to rate later, don't prompt until 2 more days have passed

## Usage

### Initialisation

Call `Appirater.init(context)` before any *Appirater* operation, such as in your main *Activity* or 
 *Application*. E.g.:

    class MyApp : Application {
    ...
        override class onCreate() {
            super.onCreate()
            Appirater.init(this)
            ...
        }
    ...
    }

### Launch

When a main/launcher *Activity* is run, you can consider this an app launch and a checkpoint where 
possibly the prompt will be shown:

    class MainActivity : Activity {
    ...
        override class onCreate() {
            super.onCreate()
            Appirater.get.onLaunch()
            ...
        }
    ...
    }

### Events

When a relevant event occurs, that shows the user is using the app, such as viewing a web page if 
your app is a browser, you can record it anywhere in the app:

    ...
    Appirater.get.onEvent()
    ...

# Installation

## As a compiled library
1. Download the `appirater-android-release.aar` and place it in your libs e.g. `app/libs/appirater-android-release.aar`
2. In your `app/build.gradle` add the module as a dependency:

        dependencies {
            [...]
            dependencies {
                implementation files('libs/appirater-android-release.aar')
            [...]
        }

## As a sub-module
1. Check check out the project as a module in your project, e.g. in directory `appirater-android`
2. In your `app/build.gradle` add the module as a dependency:

        dependencies {
            [...]
            dependencies {
                implementation project(':appirater-android')
            [...]
        }


# Customization
:warning: At a minimum modify `appirater_app_title` to read the title of your app.

## Settings
-----------------------
You can change any of these values below by re-defining it in a `values` XML file.

You can copy the `res/values/appirater-settings.xml` in to your project's `/res/values/` folder and adjust the settings to your preference.

These are all the settings that can be configured via the XML file:
 - `appirater_app_title`: your app name
 - `appirater_market_url`: URL of your app store (default: Google Play)
   - Google Play App Store = `market://details?id=%s`
   - Amazon App Store = `http://www.amazon.com/gp/mas/dl/android?p=%s
 - `appirater_days_until_prompt`: minimum days from initial launch until prompt to rate (default: 30)
 - `appirater_launches_until_prompt`: minimum launches until it prompts to rate (default: 15)
 - `appirater_untimed_events_until_prompt`: minimum events until it prompts to rate, regardless of time since launch (default: 15)
 - `appirater_timed_events_until_prompt`: minimum events until it prompts to rate, but only after the minimum days has passed (default: 15)
 - `appirater_days_before_reminding`: if user chose *Later*, minimum days before it prompts to rate again (default: 3)
 - `appirater_test_mode`: set this to *true* for testing the dialog at every launch (default: false)
 
## Colors
---------
You can change any of those values below by re-defining it in a `colors` XML file.
 - `appirater_button_start_color`: gradient start color for buttons
 - `appirater_button_end_color`: gradient end color for buttons
 - `appirater_button_text_color`: button text color
 - `appirater_title_color`: title, divider and contour color
 - `appirater_message_color`: text message color
 - `appirater_background_color`: the background color of the whole dialog

License
-------------------------
MIT/X11: http://opensource.org/licenses/MIT

This is originally forked from:
    https://github.com/kenyee/appirater-android
...which is a modified version of:
    https://github.com/drewjw81/appirater-android/
