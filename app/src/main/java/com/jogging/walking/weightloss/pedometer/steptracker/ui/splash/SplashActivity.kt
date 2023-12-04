package com.jogging.walking.weightloss.pedometer.steptracker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivitySplashBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.hasPermission
import com.jogging.walking.weightloss.pedometer.steptracker.extension.readMusicJSONFromAsset
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang.MultiLangAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.permission.PermissionAct
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import javax.inject.Inject

class SplashActivity @Inject constructor() : AbsActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
    }

    override fun initView() {
        val musicData = readMusicJSONFromAsset()
        if (musicData.size == 3){
            MyApplication.listPopMusic = musicData[0].popMusic
            MyApplication.listHiphopMusic = musicData[1].hiphopMusic
            MyApplication.listRockMusic = musicData[2].rockMusic
        }

        binding.imgLaunch.postDelayed({
            SystemUtil.setPreLanguage(this, SystemUtil.getPreLanguage(this))
            SystemUtil.setLocale(this)
            openMainActivity()
        }, 3000)
    }

    override fun initAction() {

    }

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun bindViewModel() {

    }


    private fun openMainActivity() {
        if (hasPermission(DeviceUtil.arrayStoragePermission) &&
            hasPermission(DeviceUtil.arrayLocationPermission)
        ) {
            if (!sharePrefs.isFirstOpen) {
                startActivity(MainActivityV2.getIntent(this,1))
                finish()
            } else {
                startActivity(MultiLangAct.getIntent(this, 1))
                finish()
            }
        } else {
            if (!sharePrefs.isFirstOpen) {
                startActivity(Intent(this, PermissionAct::class.java))
                finish()
            } else {
                startActivity(MultiLangAct.getIntent(this, 1))
                finish()
            }
        }
    }

}