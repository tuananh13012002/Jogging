package com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentCaloBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class CaloFr : AbsFragment<FragmentCaloBinding>() {

    private val viewModel by viewModels<StatisticViewModel>()

    override fun getLayoutRes() = R.layout.fragment_calo

    override fun initAction() {
        binding.totalCalo.isSelected=true
        binding.bestCalo.isSelected=true
        binding.averageCalo.isSelected=true

    }

    override fun initView() {
       bindData()
    }

    @SuppressLint("SetTextI18n")
    private fun bindData() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val dayOfWeekFormat = SimpleDateFormat("E", Locale.US)
        val caloForDaysOfWeek = mutableMapOf<String, Float>()
        viewModel.getRuns.observe(viewLifecycleOwner) { runs ->
            val totalCalo = DeviceUtil.calculateTotalCalories(runs)
            val avgCalo = DeviceUtil.calculateAvgCalo(runs)
            binding.progressCalo.progress= totalCalo.toInt()
            binding.totalCalo.text = String.format("%.2f", totalCalo)+" Kcal"
            binding.bestCalo.text = String.format("%.2f", runs.maxByOrNull { it.calo ?: 0f }?.calo?:0f)+" Kcal"
            binding.averageCalo.text = String.format("%.2f", avgCalo)+" Kcal"
            // Duyệt qua danh sách các đối tượng Running
            for (run in runs) {
                // Kiểm tra xem trường duration có giá trị và định dạng hợp lệ
                val duration = run.duration
                if (!duration.isNullOrBlank()) {
                    try {
                        val date = dateFormat.parse(duration)
                        val dayOfWeek = dayOfWeekFormat.format(date)
                        val calo = run.calo ?: 0f
                        if (caloForDaysOfWeek.containsKey(dayOfWeek)) {
                            caloForDaysOfWeek[dayOfWeek] = caloForDaysOfWeek[dayOfWeek]!! + calo
                        } else {
                            caloForDaysOfWeek[dayOfWeek] = calo
                        }
                    } catch (e: ParseException) {
                        // Xử lý lỗi chuyển đổi định dạng ngày
                    }
                }
            }
            val caloForMonday = caloForDaysOfWeek["Mon"] ?: 0f
            val caloForTuesday = caloForDaysOfWeek["Tue"] ?: 0f
            val caloForWednesday = caloForDaysOfWeek["Wed"] ?: 0f
            val caloForThursday = caloForDaysOfWeek["Thu"] ?: 0f
            val caloForFriday = caloForDaysOfWeek["Fri"] ?: 0f
            val caloForSaturday = caloForDaysOfWeek["Sat"] ?: 0f
            val caloForSunday = caloForDaysOfWeek["Sun"] ?: 0f
            DeviceUtil.bindChart(
                caloForMonday,
                caloForTuesday,
                caloForWednesday,
                caloForThursday,
                caloForFriday,
                caloForSaturday,
                caloForSunday,
                resources.getString(R.string.calories)+" (kcal)",
                binding.chart,
                "#F9A805"
            )
        }
    }
}