package com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_PAUSE_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_STOP_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.NOTIFICATION_ID
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.TIMER_UPDATE_INTERVAL
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

@Suppress("DEPRECATION")
class TrackingService : LifecycleService() {

    var isFirstRun = true
    var serviceKilled = false

    private val timeRunInSeconds = MutableLiveData<Long>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var baseNotificationBuilder: NotificationCompat.Builder
    private lateinit var curNotificationBuilder: NotificationCompat.Builder

    private var pendingIntent: PendingIntent? = null

    private var sharePrefs:SharePrefs?=null

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
        val timeRunInMillis = MutableLiveData<Long>()

    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    private val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            Log.d("TuanAnh start", "start: ")
            // Dịch vụ bắt đầu khi ứng dụng bắt đầu kiểu sống START
            // Thực hiện các hoạt động cần thiết ở đây
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            Log.d("TuanAnh stop", "stop: ")
            timeRunInMillis.postValue(0L)
            addEmptyPolyLine()
            // Dịch vụ dừng khi ứng dụng dừng kiểu sống STOP
            // Thực hiện các hoạt động dừng ở đây
        }
    }


    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        lifecycle.addObserver(observer)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        baseNotificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Jogging")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)

        curNotificationBuilder = baseNotificationBuilder


        isTracking.observe(this) {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        }
        sharePrefs= SharePrefs(this)
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming Service")
                        startTimer()
                    }

                }

                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                    Timber.d("Paused Service")
                }

                ACTION_STOP_SERVICE -> {
                    killService()
                    Timber.d("Stopped Service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L

    private fun startTimer() {
        addEmptyPolyLine()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                // Time difference between time now and time started
                lapTime = System.currentTimeMillis() - timeStarted
                // Post new lapTime
                timeRunInMillis.postValue(timeRun + lapTime)

                if (timeRunInMillis.value!! >= lastSecondTimeStamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply { action = ACTION_PAUSE_SERVICE }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply { action = ACTION_START_OR_RESUME_SERVICE }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if (!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder.addAction(
                R.drawable.ic_pause_black_24dp,
                notificationActionText,
                pendingIntent
            )
            notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            val request = LocationRequest().apply {
                interval = LOCATION_UPDATE_INTERVAL
                fastestInterval = FASTEST_LOCATION_INTERVAL
                priority = PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallBack,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
        }
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            Log.i("TuanAnh 4", pos.toString())
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this) {
            if (!serviceKilled) {
                val notification = curNotificationBuilder
                    .setContentText(DeviceUtil.getFormattedStopWatchTime(it * 1000))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}