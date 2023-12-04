package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity

import androidx.core.view.forEach
import com.google.android.material.tabs.TabLayoutMediator
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityMusicBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.adapters.MusicPagerAdapter
import javax.inject.Inject

class MusicActivity @Inject constructor(): AbsActivity<ActivityMusicBinding>(){

    private lateinit var musicPagerAdapter: MusicPagerAdapter

    companion object{
        var activity: MusicActivity? = null
    }

    override fun initView() {
        if (activity != null){
            activity?.finish()
        }
        activity = this
        initViewPager()
    }

    private fun initViewPager() {
        musicPagerAdapter = MusicPagerAdapter(supportFragmentManager, lifecycle)
        binding.pagerMusic.apply {
            adapter = musicPagerAdapter
        }
        TabLayoutMediator(binding.mTabLayout, binding.pagerMusic){ tab, pos ->
            when(pos){
                0 -> tab.text = resources.getString(R.string.pop)
                1 -> tab.text = resources.getString(R.string.rock)
                2 -> tab.text = resources.getString(R.string.hiphop)
            }
        }.attach()
        binding.mTabLayout.forEach {

        }
    }

    override fun initAction() {
        binding.btnBack.onClickListener {
            finish()
        }
    }

    override fun getContentView(): Int = R.layout.activity_music

    override fun bindViewModel() {

    }

}