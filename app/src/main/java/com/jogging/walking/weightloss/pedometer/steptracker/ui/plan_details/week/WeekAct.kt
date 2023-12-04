package com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityWeekBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.adapter.WeekAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.viewModel.WeekViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.TrackingAct
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Tools
import com.jogging.walking.weightloss.pedometer.steptracker.view.horizontalcalendar.HorizontalCalendarView
import java.util.Calendar
import javax.inject.Inject

class WeekAct @Inject constructor() : AbsActivity<ActivityWeekBinding>() {
    private lateinit var adapterWeek :WeekAdapter
    private val viewModel by viewModels<WeekViewModel>()

    override fun initView() {
        val type=intent.getStringExtra(TYPE_WEEK)?:""
        bindTitle(type)

        val startTime = Calendar.getInstance()
        startTime.add(Calendar.MONTH, -6)

        val endTime = Calendar.getInstance()
        endTime.add(Calendar.MONTH, 6)

        val datesToBeColored = mutableListOf<String?>()
        datesToBeColored.add(Tools.formattedDateToday)

        binding.calendar.setUpCalendar(
            startTime.timeInMillis,
            endTime.timeInMillis,
            datesToBeColored,
            sharePrefs,
            object : HorizontalCalendarView.OnCalendarListener {
                override fun onDateSelected(date: String?) {
                }
            }
        )
        viewModel.getRuns.observe(this){list->
            when(intent.getIntExtra(TYPE_PLAN,0)){
                1-> adapterWeek= WeekAdapter(list,this,sharePrefs.getListTransaction(),Pair("100", "300"))
                2-> adapterWeek= WeekAdapter(list,this,sharePrefs.getListTransaction(),Pair("500", "750"))
                3-> adapterWeek= WeekAdapter(list,this,sharePrefs.getListTransaction(),Pair("1000", "1500"))
                4-> adapterWeek= WeekAdapter(list,this,sharePrefs.getListTransaction(),Pair("2000", "2500"))
            }
            binding.rvDay.adapter = adapterWeek
        }
    }

    override fun initAction() {
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.flTracking.setOnClickListener {
            startActivity(TrackingAct.getIntent(this))
        }
    }

    override fun getContentView() = R.layout.activity_week

    override fun bindViewModel() {
    }

    private fun bindTitle(type: String) {
        when (type) {
            Constants.WEEK1 -> binding.tvTitle.text = getString(R.string.week_1)
            Constants.WEEK2 -> binding.tvTitle.text = getString(R.string.week_2)
            Constants.WEEK3 -> binding.tvTitle.text = getString(R.string.week_3)
            Constants.WEEK4 -> binding.tvTitle.text = getString(R.string.week_4)
        }
    }

    companion object {
        private const val TYPE_PLAN = "TypePlan_WeekAct"
        private const val TYPE_WEEK = "Type_WeekAct"
        fun getIntent(context: Context, type: String,typePlan:Int): Intent {
            val intent = Intent(context, WeekAct::class.java)
            intent.putExtra(TYPE_WEEK, type)
            intent.putExtra(TYPE_PLAN, typePlan)
            return intent
        }
    }
}