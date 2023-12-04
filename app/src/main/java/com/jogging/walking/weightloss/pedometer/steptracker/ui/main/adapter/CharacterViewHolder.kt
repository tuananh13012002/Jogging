package com.jogging.walking.weightloss.pedometer.steptracker.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.ResultEntity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(entity: ResultEntity) {
        Picasso.get()
            .load(entity.image)
            .into(binding.imageIv);
    }

    companion object {
        fun create(
            viewGroup: ViewGroup,
        ): CharacterViewHolder {
            return CharacterViewHolder(
                binding = ItemCharacterBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}