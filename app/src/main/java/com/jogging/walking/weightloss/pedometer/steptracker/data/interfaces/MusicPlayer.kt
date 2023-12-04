package com.jogging.walking.weightloss.pedometer.steptracker.data.interfaces

import java.io.File

interface MusicPlayer {
    fun onPlay(url: String, action:()->Unit)
    fun onPause()
    fun onResume()
    fun onStop()
}