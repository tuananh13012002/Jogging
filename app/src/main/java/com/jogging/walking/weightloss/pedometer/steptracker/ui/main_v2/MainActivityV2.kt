package com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityMainV2Binding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.HomeFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.dialog.DialogReminder
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan.PlanFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.setting.SettingFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.TrackingAct
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.isPlan
import javax.inject.Inject

class MainActivityV2 @Inject constructor() : AbsActivity<ActivityMainV2Binding>() {

    private  var dialogReminder: DialogReminder?=null

    override fun initView() {
        sharePrefs.isFirstOpen = false
        replaceFragment(HomeFr())
        val backString = intent.getBooleanExtra("back_setting", false)
        val type = intent.getIntExtra(TYPE_MAIN, 0)
        if (backString) {
            replaceFragment(SettingFr())
            binding.bottomNav.selectedItemId = R.id.setting
        }
        dialogReminder=DialogReminder(this)
        if(type==1 && isPlan(this)){
            dialogReminder?.show()
        }
    }

    override fun initAction() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFr())
                }

                R.id.plan -> {
                    replaceFragment(PlanFr())
                }

                R.id.statistic -> {
                    replaceFragment(StatisticFr())
                }

                R.id.setting -> {
                    replaceFragment(SettingFr())
                }
            }
            true
        }
        binding.flTracking.onClickListener {
            startActivity(TrackingAct.getIntent(this))
        }
    }

    fun updateBottomNavSelection(fragmentId: Int) {
        binding.bottomNav.selectedItemId = fragmentId
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun getContentView(): Int {
        return R.layout.activity_main_v2
    }

    override fun bindViewModel() {
    }

    companion object {
        private const val TYPE_MAIN = "Type_MainV2"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, MainActivityV2::class.java)
            intent.putExtra(TYPE_MAIN, type)
            return intent
        }
    }
}