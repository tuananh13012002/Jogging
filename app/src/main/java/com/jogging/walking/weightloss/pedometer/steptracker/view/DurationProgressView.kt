package com.jogging.walking.weightloss.pedometer.steptracker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.DurationProgressViewBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class DurationProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : FrameLayout(context, attrs) {
    val binding: DurationProgressViewBinding
    private var listener: ListenerShare?=null

    init {
        binding = DurationProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bindData(duration: Int) {
        when (duration){
            in 0..10 -> {
                binding.progress1.progress = duration
                binding.txtDistance.text = "$duration / 10 mins"
                DeviceUtil.animationProgress(binding.progress1).start()
            }
            in 11..500 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = duration
                binding.txtDistance.text = "$duration / 500 mins"
                DeviceUtil.animationProgress(binding.progress2).start()

            }

            in 501..1000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = duration
                binding.txtDistance.text = "$duration / 1000 mins"
                DeviceUtil.animationProgress(binding.progress3).start()

            }

            in 1001..10000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = duration
                binding.txtDistance.text = "$duration / 10000 mins"
                DeviceUtil.animationProgress(binding.progress4).start()

            }

            else -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = binding.progress4.max
                binding.progressDistanceMax.progress = duration
                binding.txtDistance.text = "$duration / 10000 mins"
                DeviceUtil.animationProgress(binding.progressDistanceMax).start()

            }
        }
        binding.icShareDuration.onClickListener {
            listener?.share(TypeShare.DURATION,binding.root)
        }
    }
    fun setListener(listenerShare: ListenerShare){
        this.listener=listenerShare

    }
}