package com.jogging.walking.weightloss.pedometer.steptracker

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.adrevenue.AppsFlyerAdRevenue
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toDateLong
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.RemoteConfigUtils
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MyApplication : DaggerApplication() {
    companion object {
        var userCurrent: User? = null
        var UNIT = ""
        var listPlan = arrayListOf<String>()
        var currentDate: String? = null

        fun resetAndPopulateListPlan(sharePrefs:SharePrefs,listPlan: ArrayList<String>, sdf: SimpleDateFormat,dateDefault:Calendar) {
            listPlan.clear()
            sharePrefs.setListTransaction(arrayListOf())
            for (i in 0 until 7) {
                val date = dateDefault.timeInMillis
                val currentDate = sdf.format(date)
                dateDefault.add(Calendar.DAY_OF_MONTH, 1)
                listPlan.add(currentDate ?: "")
            }
            MyApplication.listPlan = listPlan
            sharePrefs.setListTransaction(listPlan)
            sharePrefs.week=sharePrefs.week+1
        }
        var progressPlan=0


        var listPopMusic = ArrayList<MusicModel.Music>()
        var listRockMusic = ArrayList<MusicModel.Music>()
        var listHiphopMusic = ArrayList<MusicModel.Music>()

    }


    private val applicationInjector =
        com.jogging.walking.weightloss.pedometer.steptracker.component.DaggerAppComponent.builder()
            .application(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector
    private lateinit var sharePrefs: SharePrefs


    override fun onCreate() {
        super.onCreate()

        RemoteConfigUtils.init(this)

        AppsFlyerLib.getInstance().init(this.getString(R.string.app_flyer), null, this)
        AppsFlyerLib.getInstance().start(this)
        sharePrefs = SharePrefs(this)

        listPlan = sharePrefs.getListTransaction()

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val dateDefault = Calendar.getInstance()

        currentDate = sdf.format(Calendar.getInstance().timeInMillis)
        val isStatus = listPlan.any { data -> currentDate == data }
        val longCurrent = currentDate?.let { sdf.parse(it)?.time } ?: 0L

        if (!isStatus && DeviceUtil.isPlan(applicationContext)) {
            if (listPlan.isNotEmpty()){
                val isValid = sdf.parse(listPlan[listPlan.size - 1])?.time ?: 0L
                if (longCurrent > isValid) { resetAndPopulateListPlan(sharePrefs,listPlan,sdf,dateDefault) }
            }else{
                resetAndPopulateListPlan(sharePrefs,listPlan,sdf,dateDefault)
            }
        }

        Strings.log("TuanAnh week",sharePrefs.week)


        val afRevenueBuilder = AppsFlyerAdRevenue.Builder(this)
        AppsFlyerAdRevenue.initialize(afRevenueBuilder.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        createNotificationChanel()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundDrinkWater: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val importanceDrinkWater = NotificationManager.IMPORTANCE_HIGH
            val channelDrinkWater = NotificationChannel(
                Constants.ID_CHANNEL_MUSIC_JOGGING,
                Constants.CHANNEL_MUSIC_JOGGING,
                importanceDrinkWater
            )
            channelDrinkWater.setSound(null, null)
            channelDrinkWater.enableVibration(false)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channelDrinkWater)
        }
    }

}

