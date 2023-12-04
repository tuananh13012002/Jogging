package com.jogging.walking.weightloss.pedometer.steptracker.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemExploreBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.listener.ListenerExplore

class ExploreAdapter(
    context: Context,
    private val listenerExplore: ListenerExplore
) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    private val explores: MutableList<Pair<String, Int>> = mutableListOf(
        Pair(context.getString(R.string.bmi), R.drawable.ic_3d_bmi),
        Pair(context.getString(R.string.challenges), R.drawable.ic_3d_challenges),
        Pair(context.getString(R.string.records), R.drawable.ic_3d_record),
//        Pair(context.getString(R.string.overall), R.drawable.ic_3d_overall),
    )

    class ExploreViewHolder(val binding: ItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(explore: Pair<String, Int>, position: Int, listenerExplore: ListenerExplore) {
            binding.apply {
                binding.textView.isSelected=true
                binding.textView.text = explore.first
                binding.img.setImageResource(explore.second)
                binding.root.setOnClickListener {
                    listenerExplore.onClickExplore(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding = ItemExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return explores.size
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val explore = explores[position]
        holder.bind(explore, position, listenerExplore)
    }
}