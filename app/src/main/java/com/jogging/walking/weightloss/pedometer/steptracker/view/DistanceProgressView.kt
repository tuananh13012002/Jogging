package com.jogging.walking.weightloss.pedometer.steptracker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.DistanceProgressViewBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class DistanceProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : FrameLayout(context, attrs) {
    private var binding: DistanceProgressViewBinding
    private var listener: ListenerShare?=null

    init {
        binding = DistanceProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bindData(distance: Int) {
        when (distance) {
            in 0..100 -> {
                binding.progress1.progress = distance
                binding.txtDistance.text = "$distance / 100 m"
                DeviceUtil.animationProgress(binding.progress1).start()
            }

            in 101..10000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = distance
                binding.txtDistance.text = "$distance / 10000 m"
                DeviceUtil.animationProgress(binding.progress2).start()

            }

            in 10001..50000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = distance
                binding.txtDistance.text = "$distance / 50000 m"
                DeviceUtil.animationProgress(binding.progress3).start()

            }

            in 50001..100000 -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = distance
                binding.txtDistance.text = "$distance / 100000 m"
                DeviceUtil.animationProgress(binding.progress4).start()

            }

            else -> {
                binding.progress1.progress = binding.progress1.max
                binding.progress2.progress = binding.progress2.max
                binding.progress3.progress = binding.progress3.max
                binding.progress4.progress = binding.progress4.max
                binding.progressDistanceMax.progress = distance
                binding.txtDistance.text = "$distance / 100000 m"
                DeviceUtil.animationProgress(binding.progressDistanceMax).start()

            }
        }
        binding.icShareDistance.onClickListener {
            listener?.share(TypeShare.DISTANCE,binding.root)
        }
    }
    fun setListener(listenerShare: ListenerShare){
        this.listener=listenerShare

    }

}