package com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.DURATION_START
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_FIRST_PLAN
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_PLAN
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_PLAN1
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_PLAN2
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_PLAN3
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_RATE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.IS_VOICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.KEY_FIRST_LANGUAGE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.KEY_FIRST_OPEN
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.KEY_FIRST_PER
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.KEY_FIRST_PROFILE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.POSITION_LANG
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.TIME_START
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.USER_WEIGHT
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.WEEK
import java.lang.reflect.Type
import javax.inject.Inject

class SharePrefs @Inject constructor(private val context: Context) {
    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    var isFirstOpen: Boolean
        get() = getPref(context).getBoolean(KEY_FIRST_OPEN, true)
        set(isFirstOpen) = getPref(context).edit().putBoolean(KEY_FIRST_OPEN, isFirstOpen).apply()

    var isMultiLang: Boolean
        get() = getPref(context).getBoolean(KEY_FIRST_LANGUAGE, true)
        set(isFirstOpen) = getPref(context).edit().putBoolean(KEY_FIRST_LANGUAGE, isFirstOpen)
            .apply()

    var isIntro: Boolean
        get() = getPref(context).getBoolean(KEY_FIRST_PER, true)
        set(isFirstOpen) = getPref(context).edit().putBoolean(KEY_FIRST_PER, isFirstOpen).apply()

    var isProfile: Boolean
        get() = getPref(context).getBoolean(KEY_FIRST_PROFILE, true)
        set(isFirstOpen) = getPref(context).edit().putBoolean(KEY_FIRST_PROFILE, isFirstOpen)
            .apply()


    var isRate: Boolean
        get() = getPref(context).getBoolean(IS_RATE, false)
        set(isRate) = getPref(context).edit().putBoolean(IS_RATE, isRate).apply()

    var weight: String?
        get() = getPref(context).getString(USER_WEIGHT, "")
        set(weight) = getPref(context).edit().putString(USER_WEIGHT, weight).apply()

    var positionLang: Int
        get() = getPref(context).getInt(POSITION_LANG, 0)
        set(positionLang) = getPref(context).edit().putInt(POSITION_LANG, positionLang).apply()

    var timeStart: String?
        get() = getPref(context).getString(TIME_START, "")
        set(timeStart) = getPref(context).edit().putString(TIME_START, timeStart).apply()

    var isPlan: Boolean
        get() = getPref(context).getBoolean(IS_PLAN, false)
        set(isPlan) = getPref(context).edit().putBoolean(IS_PLAN, isPlan).apply()

    var isPlan1: Boolean
        get() = getPref(context).getBoolean(IS_PLAN1, false)
        set(isPlan1) = getPref(context).edit().putBoolean(IS_PLAN1, isPlan1).apply()

    var isPlan2: Boolean
        get() = getPref(context).getBoolean(IS_PLAN2, false)
        set(isPlan2) = getPref(context).edit().putBoolean(IS_PLAN2, isPlan2).apply()

    var isPlan3: Boolean
        get() = getPref(context).getBoolean(IS_PLAN3, false)
        set(isPlan3) = getPref(context).edit().putBoolean(IS_PLAN3, isPlan3).apply()


    fun getListTransaction(): ArrayList<String> {
        var mListTransaction: ArrayList<String> = ArrayList()
        val transaction = getPref(context).getString(IS_FIRST_PLAN, "")
        if (transaction != "") {
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            mListTransaction = Gson().fromJson(transaction, type)
        }
        return mListTransaction
    }

    fun setListTransaction(list: ArrayList<String>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        getPref(context).edit().putString(IS_FIRST_PLAN, json).apply()
    }

    var week: Int
        get() = getPref(context).getInt(WEEK, 0)
        set(week) = getPref(context).edit().putInt(WEEK, week).apply()


    var durationStart: String?
        get() = getPref(context).getString(DURATION_START, "")
        set(durationStart) = getPref(context).edit().putString(DURATION_START, durationStart).apply()

    var isVoice: Boolean
        get() = getPref(context).getBoolean(IS_VOICE, false)
        set(isVoice) = getPref(context).edit().putBoolean(IS_VOICE, isVoice).apply()

    var isShuffle: Boolean
        get() = getPref(context).getBoolean(Constants.IS_SHUFFLE, false)
        set(isShuffle) = getPref(context).edit().putBoolean(Constants.IS_SHUFFLE, isShuffle).apply()

    var isLooping: Boolean
        get() = getPref(context).getBoolean(Constants.IS_LOOPING, false)
        set(isLooping) = getPref(context).edit().putBoolean(Constants.IS_LOOPING, isLooping).apply()

    var currentMusic: Int
        get() = getPref(context).getInt(Constants.CURRENT_MUSIC, 0)
        set(currentMusic) = getPref(context).edit().putInt(Constants.CURRENT_MUSIC, currentMusic).apply()
}