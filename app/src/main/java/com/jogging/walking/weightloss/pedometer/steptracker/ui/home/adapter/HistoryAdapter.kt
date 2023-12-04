package com.jogging.walking.weightloss.pedometer.steptracker.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemDefaultBinding
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemHistoryBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.listener.ListenerHistory
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil

class HistoryAdapter(
    private val context: Context,
    private var list: MutableList<Running>,
    private val type:Int,
    private val listenerHistory: ListenerHistory
) : RecyclerView.Adapter<ViewHolder>() {
    private val hasData = list.isNotEmpty()

    companion object {
        const val VIEW_TYPE_REAL = 1
        const val VIEW_TYPE_FAKE = 2
    }

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : ViewHolder(binding.root) {
        fun bind(running: Running, position: Int) {
            binding.apply {
                Glide.with(context).load(running.imageMap).into(img)
                txtDuration.text = running.duration
                val distanceInKM = "${running.distance?.div(1000f)} km"
                txtDistance.text = distanceInKM
                val time = DeviceUtil.getFormattedStopWatchTime(running.timesInMillis ?: 0L)
                val avgSpeed = "${running.speeds} km/h"
                txtTimeAndSpeed.text = "$time    $avgSpeed"
                if (position == 2) {
                    line.alpha = 0F
                }
                binding.root.setOnClickListener {
                    listenerHistory.onClickDetail(running.id?:0L)
                }
            }
        }
    }

    inner class DefaultViewHolder(val binding: ItemDefaultBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_REAL -> {
                val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HistoryViewHolder(binding)
            }
            VIEW_TYPE_FAKE -> {
                val binding = ItemDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DefaultViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if(type==1){
            if(!hasData)
                1
            else if(list.size in 1..2)
                list.size
            else
                3
        }else{
            if(!hasData) 1 else list.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_REAL -> {
                val realViewHolder = holder as HistoryViewHolder
                val run = list[position]
                realViewHolder.bind(run,position)
            }
            VIEW_TYPE_FAKE -> {
                val defaultViewHolder = holder as DefaultViewHolder
                defaultViewHolder.binding.root.setOnClickListener {
                    Toast.makeText(context, context.getString(R.string.sample_file), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (hasData) {
            VIEW_TYPE_REAL
        } else {
            VIEW_TYPE_FAKE
        }
    }
}