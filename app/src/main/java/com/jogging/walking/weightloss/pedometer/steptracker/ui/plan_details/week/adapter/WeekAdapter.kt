package com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemDayBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.setColorResource
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil.getPreLanguage

class WeekAdapter(
    private val runs: MutableList<Running>,
    val context: Context,
    private val listDay: ArrayList<String>,
    private val dataPlan:Pair<String,String>
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {
    companion object{
        fun listPlan(context: Context,dataPlan:Pair<String,String>): MutableList<Pair<String, String>> {
            return mutableListOf(
                Pair(context.getString(R.string.day_1), context.getString(R.string.content_day_1,dataPlan.first,dataPlan.second)),
                Pair(context.getString(R.string.day_2), context.getString(R.string.content_day_2 )),
                Pair(context.getString(R.string.day_3), context.getString(R.string.content_day_3,dataPlan.first,dataPlan.second)),
                Pair(context.getString(R.string.day_4), context.getString(R.string.content_day_4)),
                Pair(context.getString(R.string.day_5), context.getString(R.string.content_day_5,dataPlan.first,dataPlan.second)),
                Pair(context.getString(R.string.day_6), context.getString(R.string.content_day_6)),
                Pair(context.getString(R.string.day_7), context.getString(R.string.content_day_7))
            )
        }
    }

    class WeekViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            day: Pair<String, String>,
            position: Int,
            runs: MutableList<Running>,
            listDay: ArrayList<String>
        ) {
            binding.apply {
                txtDay.text = day.first
                txtContent.text = day.second
                val currentDate = DeviceUtil.getCurrentDateAsString()
                val dateIndex = listDay.indexOf(currentDate)
                val isCurrentDate = dateIndex != -1

                val statusResource = if (isCurrentDate) {
                    if (position == dateIndex) R.drawable.color_status_0 else R.drawable.color_status_2
                } else {
                    R.drawable.color_status_2
                }
                val colorResource = if (isCurrentDate && position == dateIndex) R.color.accent3 else R.color.neutral3
                val colorResourceContent = if (isCurrentDate && position == dateIndex) R.color.neutral1 else R.color.neutral3

                itemView.setBackgroundResource(statusResource)
                txtDay.setColorResource(colorResource)
                txtContent.setColorResource(colorResourceContent)

                val list: List<String?> = runs.map{it.duration}
                val durationList: List<String?> = list.map { DeviceUtil.convertDateFormat(it?: "")}
                if(listDay.isNotEmpty() && listDay[0]!= DeviceUtil.getCurrentDateAsString()){
                    if (listDay.size - 1 >= position && durationList.contains(listDay[position])) {
                        icRunPlan.setImageResource(R.drawable.logo_splash)
                        icRunPlan.setColorResource(R.color.accent1)
                    } else {
                        icRunPlan.setColorResource(R.color.accent4)
                        icRunPlan.setImageResource(R.drawable.ic_no_run)
                        if(position<dateIndex) icRunPlan.beVisible() else icRunPlan.beGone()
                    }
                }else{
                    icRunPlan.beGone()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeekViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val day = listPlan(context,dataPlan)[position]
        holder.bind(context,day, position, runs, listDay)
    }

    override fun getItemCount(): Int {
        return listPlan(context,dataPlan).size
    }
}