package com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityPlanDetailsBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.setColorResource
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toDateLong
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.dialog.RestartPlanDialog
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.WeekAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.WEEK1
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.WEEK2
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.WEEK3
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.WEEK4
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil.getPreLanguage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@SuppressLint("CommitPrefEdits")
class PlanDetailsAct @Inject constructor() : AbsActivity<ActivityPlanDetailsBinding>() {

    private val viewModel by viewModels<StatisticViewModel>()
    private lateinit var dialogRestart: RestartPlanDialog
    private var type: Int = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        bindData()
        type = intent.getIntExtra(KEY_TYPE, 0)
        bindType(type)
        dialogRestart = RestartPlanDialog(this, object : RestartPlanDialog.ListenerDialog {
            override fun conFirm() {
                resetPlan()
                sharePrefs.week = 0
                sharePrefs.getListTransaction().clear()
                sharePrefs.setListTransaction(arrayListOf())
                MyApplication.listPlan = arrayListOf()
                statusPlan(DeviceUtil.isPlan(this@PlanDetailsAct))
                statusWeek(sharePrefs.week, DeviceUtil.isPlan(this@PlanDetailsAct))
                sharePrefs.durationStart = ""
                viewModel.deleteAll()
                Strings.log("TuanAnh setListTransaction", sharePrefs.getListTransaction())
            }
        })
        statusPlan(DeviceUtil.isPlan(this@PlanDetailsAct))
    }

    private fun resetPlan() {
        sharePrefs.isPlan = false
        sharePrefs.isPlan1 = false
        sharePrefs.isPlan2 = false
        sharePrefs.isPlan3 = false
    }

    private fun bindType(type: Int) {
        when (type) {
            1 -> {
                binding.img.setImageResource(R.drawable.plan1)
                binding.tvTitle.text=getString(R.string.start_running)
                binding.titlePlan.text=getString(R.string.start_running)
                binding.contentWeek1.text = getString(R.string.content_week_1, "1.6")
                binding.titleContent.text = getString(R.string._4_weeks_to_run_1_6km_continuously, "1.6")
            }

            2 -> {
                binding.img.setImageResource(R.drawable.plan2)
                binding.tvTitle.text=getString(R.string._5k_running)
                binding.titlePlan.text=getString(R.string._5k_running)
                binding.contentWeek1.text = getString(R.string.content_week_1, "5")
                binding.titleContent.text = getString(R.string._4_weeks_to_run_1_6km_continuously, "5")
            }

            3 -> {
                binding.img.setImageResource(R.drawable.plan3)
                binding.tvTitle.text=getString(R.string._5k_to_10k_running)
                binding.titlePlan.text=getString(R.string._5k_to_10k_running)
                binding.contentWeek1.text = getString(R.string.content_week_1, "10")
                binding.titleContent.text = getString(R.string._4_weeks_to_run_1_6km_continuously, "2.8")
            }

            4 -> {
                binding.img.setImageResource(R.drawable.plan4)
                binding.tvTitle.text=getString(R.string.walking_to_weight_loss)
                binding.titlePlan.text=getString(R.string.walking_to_weight_loss)
                binding.contentWeek1.text = getString(R.string.content_week_1, "3.4")
                binding.titleContent.text = getString(R.string._4_weeks_to_run_1_6km_continuously, "18")
            }
        }
    }


    private fun bindData() {
        viewModel.getRuns.observe(this) { list ->
            Strings.log("TuanAnh kjdkjfkdjfkd",list.isNotEmpty())
            list.takeIf { it.isNotEmpty() }?.let {
                if (DeviceUtil.isPlan(this@PlanDetailsAct)) {
                    val mResult = ArrayList<String>()
                    list.map {
                        if (!mResult.contains(it.duration) && DeviceUtil.convertDateFormat(it.duration ?: "").toDateLong() >= sharePrefs.durationStart?.toDateLong()!!) {
                            mResult.add(it.duration ?: "")
                        }
                    }
                    Log.d("TuanAnh ll", "initView: $mResult")
                    MyApplication.progressPlan = mResult.size * 35
                    bindProgress()
                    val progressBar = (binding.progressBar.progress / 1000f) * 100
                    binding.txtProgressbar.text = String.format("%.1f", progressBar) + " %"
                }
            }?: kotlin.run {
                MyApplication.progressPlan =0
                binding.txtProgressbar.text="0 %"
                bindProgress()
            }
            DeviceUtil.animationProgress(binding.progressBar).start()
        }
    }

    private fun bindProgress() {
        binding.progressBar.progress = MyApplication.progressPlan
        binding.circleProgressWeek1.progress = MyApplication.progressPlan
        binding.circleProgressWeek2.progress = MyApplication.progressPlan
        binding.circleProgressWeek3.progress = MyApplication.progressPlan
        binding.circleProgressWeek4.progress = MyApplication.progressPlan

    }

    override fun initAction() {
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.btnPlan.setOnClickListener {
            binding.btnPlan.beGone()
            binding.planLayout.beVisible()
            isPlan(type)
            sharePrefs.durationStart = DeviceUtil.getCurrentDateAsString()
            statusPlan(true)
            bindDay()
            statusWeek(sharePrefs.week, DeviceUtil.isPlan(this@PlanDetailsAct))
            bindData()
        }
        binding.restarBtn.setOnClickListener {
            dialogRestart.show()
        }
        binding.btnWeek1.setOnClickListener { startWeek(WEEK1) }
        binding.btnWeek2.setOnClickListener { startWeek(WEEK2) }
        binding.btnWeek3.setOnClickListener { startWeek(WEEK3) }
        binding.btnWeek4.setOnClickListener { startWeek(WEEK4) }
    }

    private fun isStartType(type: Int) {
        if(DeviceUtil.isPlan(this)){
            val isPlanVisible = when (type) {
                1 -> sharePrefs.isPlan
                2 -> sharePrefs.isPlan1
                3 -> sharePrefs.isPlan2
                else -> sharePrefs.isPlan3
            }

            binding.btnPlan.visibility = if (isPlanVisible) View.GONE else View.VISIBLE
            binding.planLayout.visibility = if (isPlanVisible) View.VISIBLE else View.GONE

            if(binding.btnPlan.isVisible && !binding.planLayout.isVisible){
                binding.btnPlan.isEnabled=false
                binding.btnPlan.setBackgroundResource(R.drawable.btn_un_enable_start)
                binding.btnPlan.setTextColor(Color.parseColor("#F6F8F8"))
                statusPlan(false)
                statusWeek(sharePrefs.week,false)
            }else{
                binding.btnPlan.isEnabled=true
                binding.btnPlan.setBackgroundResource(R.drawable.btn_bmi_calculator)
                binding.btnPlan.setTextColor(Color.parseColor("#F6F8F8"))
                statusPlan(DeviceUtil.isPlan(this@PlanDetailsAct))
                statusWeek(sharePrefs.week, DeviceUtil.isPlan(this@PlanDetailsAct))
            }
        }else{
            binding.btnPlan.isEnabled=true
            binding.btnPlan.setBackgroundResource(R.drawable.btn_bmi_calculator)
            binding.btnPlan.setTextColor(Color.parseColor("#F6F8F8"))
            statusPlan(DeviceUtil.isPlan(this@PlanDetailsAct))
            statusWeek(sharePrefs.week, DeviceUtil.isPlan(this@PlanDetailsAct))
        }
    }

    private fun isPlan(type: Int) {
        sharePrefs.isPlan = false
        sharePrefs.isPlan1 = false
        sharePrefs.isPlan2 = false
        sharePrefs.isPlan3 = false
        when (type) {
            1 -> sharePrefs.isPlan = true
            2 -> sharePrefs.isPlan1 = true
            3 -> sharePrefs.isPlan2 = true
            4 -> sharePrefs.isPlan3 = true
        }
    }

    private fun startWeek(type: String) {
        val typePlan = intent.getIntExtra(KEY_TYPE, 0)
        when (type) {
            WEEK1 -> startActivity(WeekAct.getIntent(this, WEEK1, typePlan))
            WEEK2 -> startActivity(WeekAct.getIntent(this, WEEK2, typePlan))
            WEEK3 -> startActivity(WeekAct.getIntent(this, WEEK3, typePlan))
            WEEK4 -> startActivity(WeekAct.getIntent(this, WEEK4, typePlan))
        }
    }

    private fun statusPlan(isPlan: Boolean) {
        if (isPlan) {
            binding.btnWeek1.setBackgroundResource(R.drawable.bg_explore)
            binding.btnWeek2.setBackgroundResource(R.drawable.bg_explore)
            binding.btnWeek3.setBackgroundResource(R.drawable.bg_explore)
            binding.btnWeek4.setBackgroundResource(R.drawable.bg_explore)
            binding.week1.setColorResource(R.color.neutral1)
            binding.week2.setColorResource(R.color.neutral1)
            binding.week3.setColorResource(R.color.neutral1)
            binding.week4.setColorResource(R.color.neutral1)
            binding.btnPlan.beGone()
            binding.planLayout.beVisible()
            binding.restarBtn.beVisible()
            binding.contentPlan.text = getString(R.string._10km_4_weeks)
        } else {
            binding.btnWeek1.setBackgroundResource(R.drawable.bg_week_enable)
            binding.btnWeek2.setBackgroundResource(R.drawable.bg_week_enable)
            binding.btnWeek3.setBackgroundResource(R.drawable.bg_week_enable)
            binding.btnWeek4.setBackgroundResource(R.drawable.bg_week_enable)
            binding.week1.setColorResource(R.color.neutral3)
            binding.week2.setColorResource(R.color.neutral3)
            binding.week3.setColorResource(R.color.neutral3)
            binding.week4.setColorResource(R.color.neutral3)
            binding.btnPlan.beVisible()
            binding.planLayout.beGone()
            binding.btnWeek1.isEnabled = false
            binding.btnWeek2.isEnabled = false
            binding.btnWeek3.isEnabled = false
            binding.btnWeek4.isEnabled = false
            binding.restarBtn.beGone()
            binding.contentPlan.text = getString(R.string.time_distance)
        }
    }

    private fun statusWeek(week: Int, isPlan: Boolean) {
        Strings.log("TuanAnh isPlan",isPlan)
        Strings.log("TuanAnh week",week)
        if (isPlan) {
            when (week) {
                0 -> {
                    binding.circleProgressWeek1.beGone()
                    binding.circleProgressWeek2.beGone()
                    binding.circleProgressWeek3.beGone()
                    binding.circleProgressWeek4.beGone()
                    binding.icPlanSuccess1.beVisible()
                    binding.icPlanSuccess2.beVisible()
                    binding.icPlanSuccess3.beVisible()
                    binding.icPlanSuccess4.beVisible()
                    binding.icPlanSuccess1.setImageResource(R.drawable.ic_detail_history)
                    binding.icPlanSuccess2.setImageResource(R.drawable.ic_detail_history)
                    binding.icPlanSuccess3.setImageResource(R.drawable.ic_detail_history)
                    binding.icPlanSuccess4.setImageResource(R.drawable.ic_detail_history)
                }

                1 -> {

                    binding.circleProgressWeek1.beVisible()
                    binding.icPlanSuccess1.beGone()
                    binding.btnWeek1.isEnabled = true
                    binding.btnWeek2.isEnabled = false
                    binding.btnWeek3.isEnabled = false
                    binding.btnWeek4.isEnabled = false
                }

                2 -> {
                    binding.icPlanSuccess1.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess2.beGone()
                    binding.circleProgressWeek2.beVisible()
                    binding.btnWeek2.isEnabled = true
                    binding.btnWeek1.isEnabled = false
                    binding.btnWeek3.isEnabled = false
                    binding.btnWeek4.isEnabled = false
                }

                3 -> {
                    binding.icPlanSuccess1.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess2.setImageResource(R.drawable.ic_plan_success)
                    binding.btnWeek1.isEnabled = false
                    binding.btnWeek2.isEnabled = false
                    binding.btnWeek4.isEnabled = false
                    binding.icPlanSuccess3.beGone()
                    binding.circleProgressWeek3.beVisible()
                    binding.btnWeek3.isEnabled = true
                }

                4 -> {
                    binding.icPlanSuccess1.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess2.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess3.setImageResource(R.drawable.ic_plan_success)
                    binding.btnWeek1.isEnabled = false
                    binding.btnWeek2.isEnabled = false
                    binding.btnWeek3.isEnabled = false
                    binding.icPlanSuccess4.beGone()
                    binding.circleProgressWeek4.beVisible()
                    binding.btnWeek4.isEnabled = true
                }

                else -> {
                    binding.btnWeek1.isEnabled = false
                    binding.btnWeek2.isEnabled = false
                    binding.btnWeek3.isEnabled = false
                    binding.btnWeek4.isEnabled = false
                    binding.icPlanSuccess1.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess2.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess3.setImageResource(R.drawable.ic_plan_success)
                    binding.icPlanSuccess4.setImageResource(R.drawable.ic_plan_success)
                }
            }
        } else {
            binding.circleProgressWeek1.beGone()
            binding.circleProgressWeek2.beGone()
            binding.circleProgressWeek3.beGone()
            binding.circleProgressWeek4.beGone()
            binding.icPlanSuccess1.beVisible()
            binding.icPlanSuccess2.beVisible()
            binding.icPlanSuccess3.beVisible()
            binding.icPlanSuccess4.beVisible()
            binding.icPlanSuccess1.setImageResource(R.drawable.ic_detail_history)
            binding.icPlanSuccess2.setImageResource(R.drawable.ic_detail_history)
            binding.icPlanSuccess3.setImageResource(R.drawable.ic_detail_history)
            binding.icPlanSuccess4.setImageResource(R.drawable.ic_detail_history)
        }
    }

    private fun bindDay() {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val dateDefault = Calendar.getInstance()
        val isStatus = sharePrefs.getListTransaction().any { data -> MyApplication.currentDate == data }
        val longCurrent = MyApplication.currentDate?.let { sdf.parse(it)?.time } ?: 0L

        if (!isStatus && DeviceUtil.isPlan(this@PlanDetailsAct)) {
            if (MyApplication.listPlan.isNotEmpty()) {
                val isValid = sdf.parse(MyApplication.listPlan[MyApplication.listPlan.size - 1])?.time ?: 0L
                if (longCurrent > isValid) { MyApplication.resetAndPopulateListPlan(sharePrefs,MyApplication.listPlan,sdf,dateDefault) }
            }else{
                MyApplication.resetAndPopulateListPlan(sharePrefs,MyApplication.listPlan,sdf,dateDefault)
            }
        }
        Strings.log("TuanAnh kk",sharePrefs.week)
    }

    override fun getContentView(): Int {
        return R.layout.activity_plan_details
    }

    override fun bindViewModel() {
    }

    override fun onResume() {
        super.onResume()
        statusWeek(sharePrefs.week, DeviceUtil.isPlan(this))
        bindData()
        isStartType(type)
    }


    companion object {
        private const val KEY_TYPE = "PlanDetailsAct_Type"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, PlanDetailsAct::class.java)
            intent.putExtra(KEY_TYPE, type)
            return intent
        }
    }
}