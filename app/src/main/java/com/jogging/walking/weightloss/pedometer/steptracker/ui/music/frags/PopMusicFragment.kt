package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentPopBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity.MusicPlayerActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.adapters.MusicAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import javax.inject.Inject

class PopMusicFragment @Inject constructor(): AbsFragment<FragmentPopBinding>(){

    private lateinit var musicAdapter: MusicAdapter

    companion object{
        @JvmStatic
        fun newInstance() = PopMusicFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_pop

    override fun initView() {
        initRcv()
    }

    private fun initRcv() {
        musicAdapter = MusicAdapter()
        binding.rvMusic.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = musicAdapter
        }
        musicAdapter.applyData(MyApplication.listPopMusic)
        musicAdapter.applyEvent {
            handleChooseMusic(it)
        }
    }

    private fun handleChooseMusic(music: MusicModel.Music) {
        startActivity(Intent(requireActivity(), MusicPlayerActivity::class.java).apply {
            putExtra(Constants.EXTRA_MUSIC_TO_PLAY, music)
            putExtra(Constants.EXTRA_TYPE_MUSIC, Constants.POP_MUSIC)
        })
    }

    override fun initAction() {

    }

}