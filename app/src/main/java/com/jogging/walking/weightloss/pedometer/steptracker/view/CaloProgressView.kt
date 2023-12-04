package com.jogging.walking.weightloss.pedometer.steptracker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.CaloProgressViewBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class CaloProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : FrameLayout(context, attrs) {
    val binding: CaloProgressViewBinding
    private var listener: ListenerShare?=null

    init {
        binding = CaloProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bindData(calo: Int) {
        when (calo) {
            in 0..5000 -> {
                binding.progress1.progress = calo
                binding.txtDistance.text = "$calo / 5000 kcal"
                DeviceUtil.animationProgress(binding.progress1).start()
            }

            in 5001..500000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = calo
                binding.txtDistance.text = "$calo / 500000 kcal"
                DeviceUtil.animationProgress(binding.progress2).start()

            }

            in 500001..3000000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = calo
                binding.txtDistance.text = "$calo / 3M kcal"
                DeviceUtil.animationProgress(binding.progress3).start()

            }

            in 3000001..5000000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = calo
                binding.txtDistance.text = "$calo / 5M kcal"
                DeviceUtil.animationProgress(binding.progress4).start()

            }

            else -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = binding.progress4.max
                binding.progressDistanceMax.progress = calo
                binding.txtDistance.text = "$calo / 5M kcal"
                DeviceUtil.animationProgress(binding.progressDistanceMax).start()

            }
        }
        binding.icShareCalo.onClickListener {
            listener?.share(TypeShare.CALO,binding.root)
        }
    }
    fun setListener(listenerShare: ListenerShare){
        this.listener=listenerShare

    }
}