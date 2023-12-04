package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemRecordsBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class RecordsAdapter(
    private var typeList: MutableList<String>,
    private var listRun: MutableList<Running>,
    private val context: Context,
    private val listenerShare: ListenerShare
) : RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {
    companion object {
        val dummyData = mutableListOf("distance", "duration", "calories", "speed", "pace")
    }

    inner class ViewHolder(val binding: ItemRecordsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, listRun: MutableList<Running>) {
            binding.apply {
                val maxDistanceRun = listRun.maxByOrNull { it.distance ?: 0f }
                val maxCaloRun = listRun.maxByOrNull { it.calo ?: 0f }
                val maxSpeedRun = listRun.maxByOrNull { it.speeds ?: 0f }
                val maxTimesInMillisRun = listRun.maxByOrNull { it.timesInMillis ?: 0L }
                when (item) {
                    "distance" -> {
                        icon.setImageResource(R.drawable.ic_records_distance)
                        titleRecords.text = context.getString(R.string.longest_distance)
                        tvOutputUnit.text = "(km)"
                        tvOutput.text = String.format("%.2f", maxDistanceRun?.distance?.div(1000f) ?: 0f)
                        tvDate.text = maxDistanceRun?.duration?: context.getString(R.string.date)
                        binding.icShare.onClickListener {
                            listenerShare.share(TypeShare.DISTANCE,this.root)
                        }
                    }

                    "duration" -> {
                        icon.setImageResource(R.drawable.ic_records_duration)
                        titleRecords.text = context.getString(R.string.longest_duration)
                        tvOutputUnit.text = ""
                        tvOutput.text = DeviceUtil.getFormattedStopWatchTime(maxTimesInMillisRun?.timesInMillis ?: 0L)
                        tvDate.text = maxTimesInMillisRun?.duration?:context.getString(R.string.date)
                        binding.icShare.onClickListener {
                            listenerShare.share(TypeShare.DURATION,this.root)
                        }

                    }

                    "calories" -> {
                        icon.setImageResource(R.drawable.ic_records_calories)
                        titleRecords.text = context.getString(R.string.most_calories)
                        tvOutputUnit.text = "(kcal)"
                        tvOutput.text = String.format("%.2f", maxCaloRun?.calo ?: 0f)
                        tvDate.text = maxCaloRun?.duration?:context.getString(R.string.date)
                        binding.icShare.onClickListener {
                            listenerShare.share(TypeShare.CALO,this.root)
                        }

                    }

                    "speed" -> {
                        icon.setImageResource(R.drawable.ic_records_speed)
                        titleRecords.text = context.getString(R.string.max_speed)
                        tvOutputUnit.text = "(kph)"
                        tvOutput.text = String.format("%.2f", maxSpeedRun?.speeds ?: 0f)
                        tvDate.text = maxSpeedRun?.duration?:context.getString(R.string.date)
                        binding.icShare.onClickListener {
                            listenerShare.share(TypeShare.SPEED,this.root)
                        }

                    }

                    "pace" -> {
                        icon.setImageResource(R.drawable.ic_records_pace)
                        titleRecords.text = context.getString(R.string.best_pace)
                        tvOutputUnit.text = "(min/km)"
                        val pace = maxSpeedRun?.speeds?.let { 60f.div(it) }
                        tvOutput.text = String.format("%.2f", pace ?: 0f)
                        tvDate.text = maxSpeedRun?.duration?:context.getString(R.string.date)
                        binding.icShare.onClickListener {
                            listenerShare.share(TypeShare.PACE,this.root)
                        }
                    }

                    else -> {
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = typeList[position]
        holder.bind(item, listRun)
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

}