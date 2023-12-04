package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.HiphopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.PopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.RockMusicFragment

class MusicPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val frags = arrayOf(
        PopMusicFragment.newInstance(),
        RockMusicFragment.newInstance(),
        HiphopMusicFragment.newInstance()
    )

    override fun getItemCount(): Int = frags.size

    override fun createFragment(position: Int): Fragment = frags[position]

}