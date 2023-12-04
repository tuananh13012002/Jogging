package com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.LayoutRestartDialogBinding

class RestartPlanDialog(context: Context,val listenerDialog: ListenerDialog) : Dialog(context) {
    private lateinit var binding:LayoutRestartDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding= LayoutRestartDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAction()
    }
    private fun initAction(){
        binding.btnConfirm.setOnClickListener {
            listenerDialog.conFirm()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
    interface ListenerDialog{
        fun conFirm()
    }
}