package com.jogging.walking.weightloss.pedometer.steptracker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.SpeedProgressViewBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class SpeedProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : FrameLayout(context, attrs) {
    val binding: SpeedProgressViewBinding
    private var listener: ListenerShare?=null

    init {
        binding = SpeedProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bindData(speed: Int) {
        when (speed) {
            in 0..10 -> {
                binding.progress1.progress = speed
                binding.txtDistance.text = "$speed / 10 kph"
                DeviceUtil.animationProgress(binding.progress1).start()
            }

            in 11..20 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = speed
                binding.txtDistance.text = "$speed / 20 kph"
                DeviceUtil.animationProgress(binding.progress2).start()

            }

            in 21..30 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = speed
                binding.txtDistance.text = "$speed / 30 kph"
                DeviceUtil.animationProgress(binding.progress3).start()

            }

            in 31..35 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = speed
                binding.txtDistance.text = "$speed / 35 kph"
                DeviceUtil.animationProgress(binding.progress4).start()

            }

            else -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = binding.progress4.max
                binding.progressDistanceMax.progress = speed
                binding.txtDistance.text = "$speed / 35 kph"
                DeviceUtil.animationProgress(binding.progressDistanceMax).start()

            }
        }

        binding.icShareSpeed.onClickListener {
            listener?.share(TypeShare.SPEED,binding.root)
        }
    }
    fun setListener(listenerShare: ListenerShare){
        this.listener=listenerShare

    }
}