package com.jogging.walking.weightloss.pedometer.steptracker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jogging.walking.weightloss.pedometer.steptracker.BuildConfig

@SuppressLint("StaticFieldLeak")
object RemoteConfigUtils {

    private const val KEY_REMOTE_CONFIG = "forceUpdate"
    private lateinit var remoteConfig: FirebaseRemoteConfig
    var wasUpdated = true
    fun init(context: Context) {
        remoteConfig = getFirebaseRemoteConfig(context)
    }

    private fun getFirebaseRemoteConfig(context: Context): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0
            } else {
                60 * 60
            }
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    wasUpdated = RemoteConfigUtils.remoteConfig.getBoolean(KEY_REMOTE_CONFIG)
                } catch (ex: Exception) {
                    Log.d("Error from remote", "${ex.message}")
                }
            }
        }
        return remoteConfig
    }


}