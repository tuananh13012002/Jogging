package com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentDistanceBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.bindChart
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class DistanceFr : AbsFragment<FragmentDistanceBinding>() {

    private val viewModel by viewModels<StatisticViewModel>()

    override fun getLayoutRes() = R.layout.fragment_distance

    override fun initAction() {
        binding.totalDistance.isSelected = true
        binding.bestDistance.isSelected = true
        binding.averageDistance.isSelected = true
    }

    override fun initView() {
        bindData()
    }

    @SuppressLint("SetTextI18n")
    private fun bindData() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val dayOfWeekFormat = SimpleDateFormat("E", Locale.US)
        val distancesForDaysOfWeek = mutableMapOf<String, Float>()
        viewModel.getRuns.observe(viewLifecycleOwner) { runs ->
            val totalDistance = DeviceUtil.calculateTotalDistance(runs).div(1000f)
            val avgDistance = DeviceUtil.calculateAvgDistance(runs)
            binding.progressDistance.progress= totalDistance.toInt()
            binding.totalDistance.text = String.format("%.2f", totalDistance)+" Km"
            binding.bestDistance.text = String.format("%.2f", runs.maxByOrNull { it.distance ?: 0f }?.distance?.div(1000f) ?: 0f)+" Km"
            binding.averageDistance.text = String.format("%.2f", avgDistance.div(1000f))+" Km"
            // Duyệt qua danh sách các đối tượng Running
            runs.forEach { run->
                // Kiểm tra xem trường duration có giá trị và định dạng hợp lệ
                val duration = run.duration
                if (!duration.isNullOrBlank()) {
                    try {
                        // Chuyển đổi định dạng ngày thành ngày trong tuần (Mon, Tue, Wed, ...)
                        val date = dateFormat.parse(duration)
                        val dayOfWeek = dayOfWeekFormat.format(date)
                        val distance = run.distance ?: 0f
                        if (distancesForDaysOfWeek.containsKey(dayOfWeek)) {
                            distancesForDaysOfWeek[dayOfWeek] =
                                distancesForDaysOfWeek[dayOfWeek]!! + distance
                        } else {
                            distancesForDaysOfWeek[dayOfWeek] = distance
                        }
                    } catch (e: ParseException) {
                        // Xử lý lỗi chuyển đổi định dạng ngày
                    }
                }
            }
            val distanceForMonday = distancesForDaysOfWeek["Mon"] ?: 0f
            val distanceForTuesday = distancesForDaysOfWeek["Tue"] ?: 0f
            val distanceForWednesday = distancesForDaysOfWeek["Wed"] ?: 0f
            val distanceForThursday = distancesForDaysOfWeek["Thu"] ?: 0f
            val distanceForFriday = distancesForDaysOfWeek["Fri"] ?: 0f
            val distanceForSaturday = distancesForDaysOfWeek["Sat"] ?: 0f
            val distanceForSunday = distancesForDaysOfWeek["Sun"] ?: 0f
            bindChart(
                distanceForMonday.div(1000f),
                distanceForTuesday.div(1000f),
                distanceForWednesday.div(1000f),
                distanceForThursday.div(1000f),
                distanceForFriday.div(1000f),
                distanceForSaturday.div(1000f),
                distanceForSunday.div(1000f),
                resources.getString(R.string.distance)+" (km)",
                binding.chart,
                "#62AF83"
            )
        }
    }

}