package com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentDurationBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.convertMillisToMinutes
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class DurationFr : AbsFragment<FragmentDurationBinding>() {

    private val viewModel by viewModels<StatisticViewModel>()

    override fun getLayoutRes() = R.layout.fragment_duration

    override fun initAction() {
        binding.totalDuration.isSelected = true
        binding.bestDuration.isSelected = true
        binding.averageDuration.isSelected = true
    }

    override fun initView() {
        bindData()
    }

    @SuppressLint("SetTextI18n")
    private fun bindData() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val dayOfWeekFormat = SimpleDateFormat("E", Locale.US)
        val durationForDaysOfWeek = mutableMapOf<String, Long>()
        viewModel.getRuns.observe(viewLifecycleOwner) { runs ->
            val duration = DeviceUtil.getFormattedStopWatchTime(DeviceUtil.calculateTotalDuration(runs))
            val time = DeviceUtil.formatTime(duration)
            val maxTimesInMillisRun = runs.maxByOrNull { it.timesInMillis ?: 0L }
            val bestTime = DeviceUtil.getFormattedStopWatchTime(maxTimesInMillisRun?.timesInMillis ?: 0L)
            val avgTime = DeviceUtil.calculateAvgTimeInMillis(runs)
            val avgTimeFormat = DeviceUtil.getFormattedStopWatchTime(avgTime)
            val timeWithDot = time.replace(',', '.')
            try {
                val convertedTime = timeWithDot.toFloat().toInt()
                binding.progressDuration.progress = convertedTime
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            binding.totalDuration.text = "$time Mins"
            binding.bestDuration.text = DeviceUtil.formatTime(bestTime) + " Mins"
            binding.averageDuration.text = DeviceUtil.formatTime(avgTimeFormat) + " Mins"
            // Duyệt qua danh sách các đối tượng Running
            for (run in runs) {
                // Kiểm tra xem trường duration có giá trị và định dạng hợp lệ
                val duration = run.duration
                if (!duration.isNullOrBlank()) {
                    try {
                        val date = dateFormat.parse(duration)
                        val dayOfWeek = dayOfWeekFormat.format(date)
                        val time = run.timesInMillis ?: 0L
                        if (durationForDaysOfWeek.containsKey(dayOfWeek)) {
                            durationForDaysOfWeek[dayOfWeek] = durationForDaysOfWeek[dayOfWeek]!! + time
                        } else {
                            durationForDaysOfWeek[dayOfWeek] = time
                        }
                    } catch (e: ParseException) {
                        // Xử lý lỗi chuyển đổi định dạng ngày
                    }
                }
            }
            val durationForMonday = convertMillisToMinutes(durationForDaysOfWeek["Mon"] ?: 0L)
            val durationForTuesday = convertMillisToMinutes(durationForDaysOfWeek["Tue"] ?: 0L)
            val durationForWednesday = convertMillisToMinutes(durationForDaysOfWeek["Wed"] ?: 0L)
            val durationForThursday = convertMillisToMinutes(durationForDaysOfWeek["Thu"] ?: 0L)
            val durationForFriday = convertMillisToMinutes(durationForDaysOfWeek["Fri"] ?: 0L)
            val durationForSaturday = convertMillisToMinutes(durationForDaysOfWeek["Sat"] ?: 0L)
            val durationForSunday = convertMillisToMinutes(durationForDaysOfWeek["Sun"] ?: 0L)
            DeviceUtil.bindChart(
                durationForMonday,
                durationForTuesday,
                durationForWednesday,
                durationForThursday,
                durationForFriday,
                durationForSaturday,
                durationForSunday,
                resources.getString(R.string.duration) + " (mins)",
                binding.chart,
                "#267DC0"
            )
        }
    }
}