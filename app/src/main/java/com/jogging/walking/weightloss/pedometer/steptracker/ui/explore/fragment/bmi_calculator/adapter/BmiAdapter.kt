package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Bmi
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemBmiBinding

class BmiAdapter(
    private var bmiList: MutableList<Bmi>
) : RecyclerView.Adapter<BmiAdapter.ViewHolder>() {
    companion object {
        fun dummyData(context: Context) = mutableListOf(
            Bmi(title = context.getString(R.string.bmi_1), content = "< 17", icon = "#015CCD"),
            Bmi(title = context.getString(R.string.bmi_2), content = "17 - 18.4", icon = "#03A9EE"),
            Bmi(title = context.getString(R.string.bmi_3), content = "18.5 - 24.9", icon = "#018A01"),
            Bmi(title = context.getString(R.string.bmi_4), content = "25 - 29.9", icon = "#E9CA01"),
            Bmi(title = context.getString(R.string.bmi_5), content = "30 - 34.9", icon = "#EA9C01"),
            Bmi(title = context.getString(R.string.bmi_6), content = "35 - 39.9", icon = "#FF1401"),
            Bmi(title = context.getString(R.string.bmi_7), content = "> 39.9", icon = "#8F0202"),
        )
    }

    inner class ViewHolder(val binding: ItemBmiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bmi) {
            binding.apply {
                bmiColor.setCardBackgroundColor(Color.parseColor(item.icon))
                bmiTitle.text = item.title
                bmiContent.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBmiBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bmiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bmiList[position]
        holder.bind(item)
    }
}