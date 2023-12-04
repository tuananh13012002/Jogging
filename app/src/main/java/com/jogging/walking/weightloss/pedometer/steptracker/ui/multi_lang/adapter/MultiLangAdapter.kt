package com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.LanguageEntity
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemMultiLangBinding
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil

class MultiLangAdapter(
    private var list: MutableList<LanguageEntity>,
    private val context: Context,
    var selectedPosition: Int,
    val callBack: (Int, LanguageEntity) -> Unit
) : RecyclerView.Adapter<MultiLangAdapter.LanguageViewHolder>() {

    private lateinit var sharePrefs: SharePrefs

    companion object {
        val dummyData = mutableListOf(
            LanguageEntity(name = "English", code = "en", avatar = R.drawable.color_united_kingdom),
            LanguageEntity(name = "French", code = "fr", avatar = R.drawable.color_france),
            LanguageEntity(name = "German", code = "de", avatar = R.drawable.color_germany),
            LanguageEntity(name = "Hindi", code = "hi", avatar = R.drawable.color_india),
            LanguageEntity(name = "Spanish", code = "es", avatar = R.drawable.color_spain),
            LanguageEntity(name = "Vietnamese", code = "vi", avatar = R.drawable.color_vietnam),
        )
    }

    inner class LanguageViewHolder(val binding: ItemMultiLangBinding, context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun bind(item: LanguageEntity, isSelected: Boolean) {
            binding.apply {
                if (isSelected) {
                    SystemUtil.setPreLanguage(context, item.code)
                    SystemUtil.setLocale(context)
                    itemLangImgCheck.setImageResource(R.drawable.checked)
                    itemLang.setBackgroundResource(R.drawable.bg_border_lang)
                } else {
                    itemLangImgCheck.setImageResource(R.drawable.not_checked)
                    itemLang.setBackgroundResource(R.drawable.bg_border_lang_unselect)
                }
                itemLangToTransTvName.text = item.name
                item.avatar?.let { itemLangToTransImgAva.setImageResource(it) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LanguageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMultiLangBinding.inflate(layoutInflater, parent, false)
        return LanguageViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        sharePrefs=SharePrefs(context)
        val item = list[position]
        holder.bind(item, position == selectedPosition)
        holder.binding.root.setOnClickListener {
            list[selectedPosition].isSelected = false
            selectedPosition = position
            list[selectedPosition].isSelected = true
            notifyDataSetChanged()
            callBack(position, item)
            sharePrefs.positionLang=position
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}