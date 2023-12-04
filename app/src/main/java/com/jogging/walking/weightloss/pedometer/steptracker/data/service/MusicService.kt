package com.jogging.walking.weightloss.pedometer.steptracker.data.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.broadcast.MusicReceiver
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toast
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.manager.MusicPlayerManager
import com.jogging.walking.weightloss.pedometer.steptracker.ui.splash.SplashActivity
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MusicService: Service() {
    private var mPlayer: MusicPlayerManager? = null
    private lateinit var handler: Handler
    private val mListMusic = ArrayList<MusicModel.Music>()
    private lateinit var sharePrefs: SharePrefs
    private var isPlaying = false
    private var mMusic: MusicModel.Music? = null
    private var isNext = false
    private var mType = ""

    companion object{
        var isRunning = false
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        sharePrefs = SharePrefs(this)
        mPlayer = MusicPlayerManager(this)
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(updateProgress, 1000)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.extras != null){
            handleToBackground(intent)
            handleAction(intent)
            handleSeekTo(intent)
            handleDetectService(intent)
            handleReplay()
        }
        return START_NOT_STICKY
    }

    private fun handleDetectService(intent: Intent) {
        if (mMusic != null){
            if (intent.extras?.getBoolean(Constants.EXTRA_DETECT_SERVICE_EXIST) == true){
                sendToGetMusicCurrent(mMusic!!, mType, isPlaying)
            }
        }
    }

    private fun handleToBackground(intent: Intent) {
        // Type to get type of list music
        val typeOfMusic = intent.extras?.getString(Constants.TYPE_OF_MUSIC)
        if (typeOfMusic != null){
            mType = typeOfMusic
            mListMusic.clear()
            mListMusic.addAll(getTypeOfListCurrent(typeOfMusic))
            handleShuffle()
        }
        // Music current
        val music =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.extras?.getParcelable(Constants.EXTRA_MUSIC_TO_SERVICE, MusicModel.Music::class.java)
            else
                intent.extras?.getParcelable(Constants.EXTRA_MUSIC_TO_SERVICE)
        if (music != null){
            mMusic = music
        }
        if (music != null){
            isPlaying = true
            try {
                mPlayer?.onStop()
                mPlayer?.onPlay(mMusic!!.url){
                    sendToSetView(Constants.ACTION_FIRST_PLAY)
                }
                seekTo(1)
                sharePrefs.currentMusic = mListMusic.indexOf(mMusic)
                startForeground(1, notificationMusic(this@MusicService, mMusic!!.name ?: "Could be found name"))
                isRunning = true
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }

    private fun getTypeOfListCurrent(typeOfMusic: String): ArrayList<MusicModel.Music> {
        return when(typeOfMusic){
            Constants.POP_MUSIC -> MyApplication.listPopMusic
            Constants.ROCK_MUSIC -> MyApplication.listRockMusic
            Constants.HIPHOP -> MyApplication.listHiphopMusic
            else -> ArrayList()
        }
    }

    private fun handleSeekTo(intent: Intent) {
        val numSeekTo = intent.extras?.getInt(Constants.ACTION_SEEK_TO, -1) ?: -1
        if (numSeekTo != -1){
            seekTo(numSeekTo)
        }
    }

    private fun handleAction(intent: Intent) {
        when(intent.extras?.getInt(Constants.ACTION_MUSIC_SERVICE)){
            Constants.ACTION_RESUME -> {
                isPlaying = true
                mPlayer?.onResume()
                if (mMusic != null){
                    startForeground(1, notificationMusic(this, mMusic?.name ?: "Could be found name"))
                }
                sendToSetView(Constants.ACTION_RESUME)
            }
            Constants.ACTION_PAUSE -> {
                isPlaying = false
                mPlayer?.onPause()
                if (mMusic != null){
                    startForeground(1, notificationMusic(this, mMusic?.name ?: "Could be found name"))
                }
                sendToSetView(Constants.ACTION_PAUSE)
            }
            Constants.ACTION_PREVIOUS -> {
                handlePrevious()
            }
            Constants.ACTION_NEXT -> {
                handleNext()
            }
            Constants.ACTION_SHUFFLE -> {
                handleShuffle()
            }
            Constants.ACTION_REPLAY -> {
                handleReplay()
            }
        }
    }

    private fun handleReplay() {
        mPlayer?.getMediaPlayer()?.isLooping = sharePrefs.isLooping
    }

    private fun handleShuffle() {
        if (sharePrefs.isShuffle){
            mListMusic.shuffle()
            sharePrefs.currentMusic = mListMusic.indexOf(mMusic)
        }else{
            mListMusic.clear()
            mListMusic.addAll(getTypeOfListCurrent(mType))
        }
    }

    private fun handleNext() {
        sharePrefs.currentMusic = mListMusic.indexOf(mMusic)
        mPlayer?.onStop()
        if (sharePrefs.currentMusic != -1){
            mMusic = if (sharePrefs.currentMusic  == mListMusic.size - 1){
                sharePrefs.currentMusic = 0
                mPlayer?.onPlay(mListMusic[sharePrefs.currentMusic].url){
                    sendToSetView(Constants.ACTION_FIRST_PLAY)
                    handlePlayedWithAction(Constants.ACTION_NEXT)
                    isNext = false
                }
                mListMusic[sharePrefs.currentMusic]
            }else{
                sharePrefs.currentMusic += 1
                mPlayer?.onPlay(mListMusic[sharePrefs.currentMusic].url){
                    sendToSetView(Constants.ACTION_FIRST_PLAY)
                    handlePlayedWithAction(Constants.ACTION_NEXT)
                    isNext = false
                }
                mListMusic[sharePrefs.currentMusic]
            }
        }
    }
    private fun handlePrevious() {
        sharePrefs.currentMusic = mListMusic.indexOf(mMusic)
        mPlayer?.onStop()
        if (sharePrefs.currentMusic != -1){
            mMusic = if (sharePrefs.currentMusic == 0){
                sharePrefs.currentMusic = mListMusic.size - 1
                mPlayer?.onPlay(mListMusic[sharePrefs.currentMusic].url){
                    sendToSetView(Constants.ACTION_FIRST_PLAY)
                    handlePlayedWithAction(Constants.ACTION_PREVIOUS)
                    isNext = false
                }
                mListMusic[sharePrefs.currentMusic]
            }else{
                sharePrefs.currentMusic -= 1
                mPlayer?.onPlay(mListMusic[sharePrefs.currentMusic - 1].url){
                    sendToSetView(Constants.ACTION_FIRST_PLAY)
                    handlePlayedWithAction(Constants.ACTION_PREVIOUS)
                    isNext = false
                }
                mListMusic[sharePrefs.currentMusic - 1]
            }
        }
    }

    private fun handlePlayedWithAction(action: Int) {
        isPlaying = true
        startForeground(1, notificationMusic(this, mMusic?.name!!))
        seekTo(0)
        sendToSetView(action, mMusic)
    }

    private val updateProgress = object : Runnable {
        override fun run() {
            val progress = mPlayer?.getCurrentPosition()
            if (progress != null){
                if (mPlayer != null && progress == mPlayer?.getMediaPlayer()?.duration && !isNext && progress != 0){
                    if (!sharePrefs.isLooping){
                        isNext = true
                        seekTo(0)
                        sendToSetView(Constants.ACTION_AUTO_NEXT)
                        handleNext()
                    }
                }
                sendProgressToForeground(progress)
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun sendProgressToForeground(progress: Int) {
        val intent = Intent("${this.packageName}.PROGRESS_UPDATE")
        intent.putExtra(Constants.MUSIC_PROGRESS, progress)
        sendBroadcast(intent)
    }

    private fun sendToSetView(action: Int, mMusic: MusicModel.Music? = null){
        val intent = Intent("${this.packageName}.PROGRESS_UPDATE")
        intent.putExtra(Constants.MUSIC_ACTION, action)
        intent.putExtra(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY, mMusic)
        sendBroadcast(intent)
    }

    private fun sendToGetMusicCurrent(music: MusicModel.Music, type: String, action: Boolean){
        val intent = Intent("${this.packageName}.GET_MUSIC")
        intent.putExtra(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY, music)
        intent.putExtra(Constants.EXTRA_TYPE_MUSIC_TO_ACTIVITY, type)
        intent.putExtra(Constants.EXTRA_ACTION_MUSIC_TO_ACTIVITY, action)
        sendBroadcast(intent)
    }

    private fun seekTo(pos: Int){
        mPlayer?.getMediaPlayer()?.seekTo(pos)
    }

    private fun notificationMusic(context: Context, musicName: String): Notification {
        val intentToActivity = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentToActivity,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val remoteViews = RemoteViews(context.packageName, R.layout.music_notification)
        remoteViews.setTextViewText(R.id.tv_name, musicName)

        // Actions
        remoteViews.setOnClickPendingIntent(R.id.btn_previous, getPendingIntent(this, Constants.ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getPendingIntent(this, Constants.ACTION_NEXT))
        if (isPlaying){
            remoteViews.setImageViewResource(R.id.img_interact, R.drawable.ic_pause)
            remoteViews.setOnClickPendingIntent(R.id.btn_interact, getPendingIntent(this, Constants.ACTION_PAUSE))
        }else{
            remoteViews.setImageViewResource(R.id.img_interact, R.drawable.ic_play)
            remoteViews.setOnClickPendingIntent(R.id.btn_interact, getPendingIntent(this, Constants.ACTION_RESUME))
        }

        return NotificationCompat.Builder(context, Constants.ID_CHANNEL_MUSIC_JOGGING)
        .setSmallIcon(R.drawable.ic_logo_notification)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCustomContentView(remoteViews)
        .setSound(null)
        .build()
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent{
        val intent = Intent(this, MusicReceiver::class.java).apply {
            putExtra(Constants.ACTION_MUSIC, action)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.getBroadcast(context.applicationContext, action, intent, PendingIntent.FLAG_MUTABLE)
            else
                PendingIntent.getBroadcast(context.applicationContext, action, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        mPlayer?.onStop()
    }
}