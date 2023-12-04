package com.jogging.walking.weightloss.pedometer.steptracker.data.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jogging.walking.weightloss.pedometer.steptracker.data.service.MusicService
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants

class MusicReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.extras?.getInt(Constants.ACTION_MUSIC)

        val intentService = Intent(context, MusicService::class.java).apply {
            putExtra(Constants.ACTION_MUSIC_SERVICE, action)
        }
        context.startService(intentService)
    }

}