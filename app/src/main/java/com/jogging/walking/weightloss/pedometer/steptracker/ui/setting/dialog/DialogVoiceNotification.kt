package com.jogging.walking.weightloss.pedometer.steptracker.ui.setting.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.DialogVoiceBinding

class DialogVoiceNotification(context: Context) : Dialog(context) {
    private lateinit var binding: DialogVoiceBinding
    private var sharePrefs:SharePrefs?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogVoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePrefs= SharePrefs(context)
        initAction()
        binding.txtTitle.isSelected=true

    }

    private fun initAction() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }
}