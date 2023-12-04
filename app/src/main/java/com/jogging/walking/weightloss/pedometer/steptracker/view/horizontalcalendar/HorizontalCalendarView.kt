package com.jogging.walking.weightloss.pedometer.steptracker.view.horizontalcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.HorizontalCalendarModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Tools
import com.jogging.walking.weightloss.pedometer.steptracker.view.horizontalcalendar.adapter.HorizontalCalendarAdapter
import java.text.SimpleDateFormat
import java.util.Calendar

class HorizontalCalendarView : LinearLayout {
    private var attributeSet: AttributeSet? = null
    private var recyclerView: RecyclerView? = null

    interface OnCalendarListener {
        fun onDateSelected(date: String?)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attributeSet = attrs
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attributeSet = attrs
        init(context)
    }

    private fun init(context: Context) {
        val view = inflate(context, R.layout.horizontal_calendar, null)
        val textView = view.findViewById<TextView>(R.id.title)
        recyclerView = view.findViewById(R.id.rv)
        view.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (attributeSet != null) {
            val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalCalendarView)
            textView.text = attrs.getString(R.styleable.HorizontalCalendarView_text)
            attrs.recycle()
        } else {
            textView.text = "No Text Provided"
        }
        textView.visibility = GONE
        addView(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpCalendar(
        start: Long,
        end: Long,
        dates: MutableList<String?>,
        sharePrefs: SharePrefs,
        onCalendarListener: OnCalendarListener?,
    ) {
        val c = Calendar.getInstance()
        c.timeInMillis = start
        val list: MutableList<HorizontalCalendarModel> = mutableListOf()
        val today: Long = Tools.getTimeInMillis(Tools.formattedDateToday)
        var current = start
        var i = 0
        var pos = 0
        while (current < end) {
            val c1 = Calendar.getInstance()
            c1.timeInMillis = start
            c1.add(Calendar.DATE, i)
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            if (sdf.format(c1.timeInMillis).equals(sdf.format(today), ignoreCase = true)) {
                pos = i
                Log.d("Postion", pos.toString() + "")
            }
            val model = HorizontalCalendarModel(c1.timeInMillis)
            if (dates.contains(sdf.format(c1.timeInMillis))) {
                model.status = 1
            }
            list.add(model)
            current = c1.timeInMillis
            i++
            Log.d("Setting data", sdf.format(c1.timeInMillis))
        }
        val adapter = HorizontalCalendarAdapter(list, context)
        adapter.setOnCalendarListener(onCalendarListener)
        adapter.setSharePrefs(sharePrefs)
//        val snapHelper: SnapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()
        recyclerView?.scrollToPosition(pos)
        recyclerView?.smoothScrollToPosition(if((pos-2)>0) pos-2 else pos)
    }
}