package com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.DialogReminderBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.adapter.WeekAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class DialogReminder(context: Context) : Dialog(context) {
    private lateinit var binding: DialogReminderBinding
    private var sharePrefs:SharePrefs?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePrefs= SharePrefs(context)
        initView()
        initAction()

    }

    private fun initAction() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initView() {
        val currentDate = DeviceUtil.getCurrentDateAsString()
        val dateIndex = sharePrefs?.getListTransaction()?.indexOf(currentDate)?:0
        val planValue = when {
            sharePrefs?.isPlan == true -> Pair("100", "300")
            sharePrefs?.isPlan1 == true -> Pair("500", "750")
            sharePrefs?.isPlan2 == true -> Pair("1000", "1500")
            else -> Pair("2000", "2500")
        }
        binding.txtContentReminder.text = WeekAdapter.listPlan(context, planValue)[dateIndex].second
    }
}