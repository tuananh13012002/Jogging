package com.jogging.walking.weightloss.pedometer.steptracker.utils

import android.graphics.Color
import com.jogging.walking.weightloss.pedometer.steptracker.R

object Constants {
    const val IS_RATE = "is_rate"
    const val KEY_FIRST_OPEN = "first_open_app"
    const val KEY_FIRST_LANGUAGE = "first_language_app"
    const val KEY_FIRST_PER = "first_intro_app"
    const val KEY_FIRST_PROFILE = "first_profile_app"
    const val USER_WEIGHT = "weight_user"
    const val POSITION_LANG = "position_lang"
    const val HIPHOP = "hip_hop"
    const val POP_MUSIC = "pop_music"
    const val ROCK_MUSIC = "rock_music"

    const val TIME_START = "time_start"

    const val POLICY_URL = "http://noithatgiagoc.net/policy"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    // Action notification
    const val PREVIOUS = 1
    const val PAUSE = 2
    const val NEXT = 3
    const val PLAY = 4
    const val RESUME = 5

    // Receiver data broadcast
    const val ACTION_MUSIC = "action_music"
    const val ACTION_MUSIC_SERVICE = "action_music_service"
    const val EXTRA_DATA_SERVICE_TO_ACTIVITY = "data_service_to_activity"
    const val EXTRA_TYPE_MUSIC_TO_ACTIVITY = "type_of_music"
    const val EXTRA_ACTION_MUSIC_TO_ACTIVITY = "action_music_to_activity"
    const val EXTRA_DETECT_SERVICE_EXIST = "detect_service_exist"
    const val ACTION_MUSIC_CURRENT = "action_music_current"

    const val TIMER_UPDATE_INTERVAL = 50L

    const val SHARED_PREFERENCES_NAME = "shared_pref"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val ARG_OBJECT = "object"

    const val WEEK1="week1"
    const val WEEK2="week2"
    const val WEEK3="week3"
    const val WEEK4="week4"

    const val IS_PLAN = "is_plan"
    const val IS_PLAN1 = "is_plan1"
    const val IS_PLAN2 = "is_plan2"
    const val IS_PLAN3 = "is_plan3"
    const val IS_FIRST_PLAN = "is_first_plan"
    const val WEEK = "week"
    const val DURATION_START = "duration"
    const val IS_VOICE = "is_voice"
    const val IS_SHUFFLE = "is_shuffle"
    const val IS_LOOPING = "is_looping"
    const val FROM_TRACKING = "from_tracking"

    /** Shared Preferences */
    const val CURRENT_MUSIC = "current_music"

    /** Extra data */
    const val EXTRA_MUSIC_TO_PLAY = "EXTRA_MUSIC_TO_PLAY"
    const val EXTRA_TYPE_MUSIC = "EXTRA_TYPE_MUSIC"
    const val EXTRA_MUSIC_TO_SERVICE = "EXTRA_MUSIC_TO_SERVICE"
    const val EXTRA_FROM_TRACKING = "EXTRA_FROM_TRACKING"

    /** Channel music */

    const val CHANNEL_MUSIC_JOGGING = "music jogging channel"
    const val ID_CHANNEL_MUSIC_JOGGING = "CHANNEL_MUSIC_JOGGING"

    /** Action */
    const val ACTION_RESUME = 1
    const val ACTION_PAUSE = 2
    const val ACTION_PREVIOUS = 3
    const val ACTION_NEXT = 4
    const val ACTION_REPLAY = 5
    const val ACTION_SHUFFLE = 6
    const val ACTION_AUTO_NEXT = 7
    const val ACTION_FIRST_PLAY = 8
    const val TYPE_OF_MUSIC = "type_of_music"
    const val ACTION_SEEK_TO = "seek_to"
    const val MUSIC_PROGRESS = "music_progress"
    const val MUSIC_ACTION = "music_action"

}