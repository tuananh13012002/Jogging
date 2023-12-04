package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemMusicBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onEventClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toDuration

class MusicAdapter: Adapter<MusicAdapter.MusicViewHolder>() {

    private val listMusic = ArrayList<MusicModel.Music>()
    private var onClickItem: ((MusicModel.Music) -> Unit)? = null

    fun applyData(listMusic: ArrayList<MusicModel.Music>){
        this.listMusic.clear()
        this.listMusic.addAll(listMusic)

    }

    fun applyEvent(onClickItem: (MusicModel.Music) -> Unit){
        this.onClickItem = onClickItem
    }

    inner class MusicViewHolder(private val binding: ItemMusicBinding): ViewHolder(binding.root){
        fun binds(music: MusicModel.Music){
            binding.tvDuration.text =  music.duration
//                .toDuration()
            binding.tvName.text = music.name
            binding.root.onClickListener {
                onClickItem?.let { onEvent -> onEvent(music) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listMusic.size

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.binds(listMusic[position])
    }

}