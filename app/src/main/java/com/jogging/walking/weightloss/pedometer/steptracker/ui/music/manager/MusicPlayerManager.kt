package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.interfaces.MusicPlayer
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toast
import com.jogging.walking.weightloss.pedometer.steptracker.utils.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerManager(
    private val context: Context
): MusicPlayer {

    private var mPlayer: MediaPlayer? = null

    override fun onPlay(url: String, action:()->Unit) {
        play(url, action)
    }

    private fun play(url: String, action:()->Unit){
        mPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            try{
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    action()
                }
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }

    override fun onPause() {
        if(mPlayer != null){
            mPlayer?.pause()
        }else{
            context.toast(R.string.something_wrong)
        }
    }

    override fun onResume() {
        if(mPlayer != null){
            mPlayer?.start()
        }else{
            context.toast(R.string.something_wrong)
        }
    }

    override fun onStop() {
        if (mPlayer != null){
            mPlayer?.stop()
            mPlayer?.release()
        }
    }

    fun getMediaPlayer() = mPlayer

    fun getCurrentPosition() = mPlayer?.currentPosition

}