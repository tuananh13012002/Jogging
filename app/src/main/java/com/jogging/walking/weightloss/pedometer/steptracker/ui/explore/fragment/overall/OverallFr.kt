package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.overall

import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentOverallBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.DetailsAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.viewmodel.RecordViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.adapter.HistoryAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.listener.ListenerHistory
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import javax.inject.Inject

class OverallFr @Inject constructor() : AbsFragment<FragmentOverallBinding>(), ListenerHistory {
    private val viewModel by viewModels<RecordViewModel>()
    override fun getLayoutRes(): Int {
        return R.layout.fragment_overall
    }

    override fun initAction() {
    }

    override fun initView() {
        binding.tvDuration.isSelected = true
        binding.tvPace.isSelected = true
        binding.tvDistance.isSelected = true
        binding.tvCalories.isSelected = true
        bindHistory()
    }

    private fun bindOverall(list: MutableList<Running>) {
        val totalCalories = DeviceUtil.calculateTotalCalories(list)
        val totalDistance = DeviceUtil.calculateTotalDistance(list).div(1000f)
        val duration = DeviceUtil.getFormattedStopWatchTime(DeviceUtil.calculateTotalDuration(list))
        val time = DeviceUtil.formatTime(duration)
        val avgSpeed = DeviceUtil.calculateAvgSpeeds(list)
        val pace = DeviceUtil.calculatePaceFromSpeed(avgSpeed) ?: 0f
        binding.tvCalories.text = DeviceUtil.formattedValue(totalCalories)
        binding.tvDistance.text = DeviceUtil.formattedValue(totalDistance)
        binding.tvSpeed.text = DeviceUtil.formattedValue(avgSpeed)
        binding.tvPace.text = DeviceUtil.formattedValue(pace)
        binding.tvDuration.text = time
    }

    private fun bindHistory() {
        viewModel.getRuns.observe(this) {
            if (it.isNotEmpty()) binding.progress.root.beGone()
            val historyAdapter = HistoryAdapter(requireContext(), it, 2, this)
            binding.rvHistory.adapter = historyAdapter
            bindOverall(it)
        }
    }

    override fun onClickDetail(id: Long) {
        startActivity(DetailsAct.getIntent(requireContext(), id,1))
    }
}