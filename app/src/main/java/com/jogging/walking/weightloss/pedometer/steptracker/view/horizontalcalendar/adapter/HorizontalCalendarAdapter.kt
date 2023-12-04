package com.jogging.walking.weightloss.pedometer.steptracker.view.horizontalcalendar.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.HorizontalCalendarModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.HorizontalCalendarListItemBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toDateLong
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.getCurrentDateAsString
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import com.jogging.walking.weightloss.pedometer.steptracker.view.horizontalcalendar.HorizontalCalendarView
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
internal class HorizontalCalendarAdapter(
    list: MutableList<HorizontalCalendarModel>,
    context: Context
) :
    RecyclerView.Adapter<HorizontalCalendarAdapter.MyViewHolder>() {
    private val list: MutableList<HorizontalCalendarModel>
    private val context: Context
    private var onCalendarListener: HorizontalCalendarView.OnCalendarListener? = null
    private var sharePrefs: SharePrefs? = null

    fun setSharePrefs(sharePrefs: SharePrefs) {
        this.sharePrefs = sharePrefs
    }

    fun setOnCalendarListener(onCalendarListener: HorizontalCalendarView.OnCalendarListener?) {
        this.onCalendarListener = onCalendarListener
    }

    init {
        this.list = list
        this.context = context
    }

    inner class MyViewHolder(val binding: HorizontalCalendarListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: HorizontalCalendarModel, position: Int) {
            val display = (context as Activity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            binding.parent.minimumWidth = Math.round((width / 7).toFloat())
            val sdf = SimpleDateFormat("dd MMM EEE")
            Strings.log(sdf.format(model.timeinmilli))
            val sdf1 = SimpleDateFormat("dd-MM-yyyy")
            binding.date.text = sdf.format(model.timeinmilli).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            binding.month.text = sdf.format(model.timeinmilli).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            binding.day.text = sdf.format(model.timeinmilli).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]

            val posCurrentDate = list.indexOfFirst { it.status == 1 }
            if(position==posCurrentDate){
                binding.date.setTextColor(context.getColor(R.color.white))
                binding.month.setTextColor(context.getColor(R.color.white))
                binding.day.setTextColor(context.getColor(R.color.white))
                binding.parent.setBackgroundResource(R.drawable.color_status_1)
            }else if(position<posCurrentDate){
                binding.date.setTextColor(context.getColor(R.color.neutral2))
                binding.month.setTextColor(context.getColor(R.color.grey_600))
                binding.day.setTextColor(context.getColor(R.color.neutral3))
                binding.parent.setBackgroundResource(R.drawable.color_status_0)
            }else{
                binding.date.setTextColor(context.getColor(R.color.neutral2))
                binding.month.setTextColor(context.getColor(R.color.grey_600))
                binding.day.setTextColor(context.getColor(R.color.neutral3))
                binding.parent.setBackgroundResource(R.drawable.color_status_2)
            }

            val dateCalendar = sdf1.format(model.timeinmilli)
            val listPlan = sharePrefs?.getListTransaction()
            Strings.log("TuanAnh jjjj",listPlan)
            listPlan?.let { firstPlan ->
                if(firstPlan.isNotEmpty()){
                    if(firstPlan[0]!=getCurrentDateAsString()){
                        if (dateCalendar == firstPlan[0]) {
                            binding.date.setTextColor(context.getColor(R.color.neutral2))
                            binding.month.setTextColor(context.getColor(R.color.grey_600))
                            binding.day.setTextColor(context.getColor(R.color.neutral3))
                            binding.parent.setBackgroundResource(R.drawable.color_status_3)
                        }
                    }
                }
            }
            binding.parent.setOnClickListener { onCalendarListener?.onDateSelected(sdf1.format(model.timeinmilli)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HorizontalCalendarListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: HorizontalCalendarModel = list[position]
        holder.bind(model, position)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}