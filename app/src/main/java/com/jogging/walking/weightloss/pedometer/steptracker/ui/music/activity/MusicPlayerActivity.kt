package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.service.MusicService
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityMusicPlayerBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Tools
import com.jogging.walking.weightloss.pedometer.steptracker.utils.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MusicPlayerActivity @Inject constructor(): AbsActivity<ActivityMusicPlayerBinding>() {
    private lateinit var rotateAnim: ObjectAnimator
    private var mMusic: MusicModel.Music? = null
    private var typeOfMusic = ""
    private var isPlaying = false
    private var wasRegistered = false
    private var isSeeking = false
    private var mListMusic = ArrayList<MusicModel.Music>()

    companion object{
        var activity: MusicPlayerActivity? = null
    }

    override fun initView() {
        if (activity != null){
            activity?.finish()
        }
        activity = this
        binding.musicPlayerActivity = this
        rotateAnim = ObjectAnimator.ofFloat(binding.imgDisk, "rotation", 0f, 360f)
        rotateAnim.apply {
            duration = 3500
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        initUI()
        eventSeekbar()
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, IntentFilter("${this.packageName}.PROGRESS_UPDATE"))
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnListMusic.onClickListener {
            startActivity(Intent(this, MusicActivity::class.java))
        }
        binding.btnNext.onClickListener {
            handleNext()
        }
        binding.btnPrevious.onClickListener {
            handlePrevious()
        }
        binding.btnInteract.onClickListener {
            eventPlay()
        }
        binding.btnShuffle.onClickListener {
            handleShuffle()
        }
        binding.btnRepeatMode.onClickListener {
            handleReplay()
        }
    }

    private fun initUI() {
        if (intent.extras !=  null){
            binding.lavLoading.beVisible()
            mMusic =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    intent.extras?.getParcelable(Constants.EXTRA_MUSIC_TO_PLAY, MusicModel.Music::class.java)
                else
                    intent.extras?.getParcelable(Constants.EXTRA_MUSIC_TO_PLAY)
            typeOfMusic = intent.extras?.getString(Constants.EXTRA_TYPE_MUSIC) ?: ""
            val fromTracking = intent.extras?.getString(Constants.EXTRA_FROM_TRACKING)
            val currentAction = intent.extras?.getBoolean(Constants.ACTION_MUSIC_CURRENT)

            binding.tvName.text = mMusic?.name
            binding.tvStart.text = "00:00"
            binding.tvEnd.text = mMusic?.duration
            initImgMusic()
            if (fromTracking != Constants.FROM_TRACKING){
                startService(Intent(this, MusicService::class.java).apply {
                    putExtra(Constants.EXTRA_MUSIC_TO_SERVICE, mMusic)
                    putExtra(Constants.TYPE_OF_MUSIC, typeOfMusic)
                })
            }else{
                if (currentAction == true){
                    isPlaying = false
                    binding.imgInteract.setImageResource(R.drawable.ic_pause)
                    rotateAnim.start()
                }else{
                    isPlaying = true
                    binding.imgInteract.setImageResource(R.drawable.ic_play)
                    rotateAnim.cancel()
                }
            }
        }
        if(sharePrefs.isShuffle){
            binding.imgShuffle.setImageResource(R.drawable.ic_shuffle_active)
        }else{
            binding.imgShuffle.setImageResource(R.drawable.ic_shuffle)
        }
        if (sharePrefs.isLooping){
            binding.imgRepeatMode.setImageResource(R.drawable.ic_repeate_active)
        }else{
            binding.imgRepeatMode.setImageResource(R.drawable.ic_replay)
        }
    }

    private fun initImgMusic() {
        when(typeOfMusic){
            Constants.HIPHOP -> {
                mListMusic = MyApplication.listHiphopMusic
                binding.imgCoverImage.setImageResource(R.drawable.img_cover_hiphop_music)
                binding.tvTitle.text = resources.getString(R.string.hiphop)
            }
            Constants.POP_MUSIC -> {
                mListMusic = MyApplication.listPopMusic
                binding.imgCoverImage.setImageResource(R.drawable.img_cover_pop_music)
                binding.tvTitle.text = resources.getString(R.string.pop)
            }
            Constants.ROCK_MUSIC -> {
                mListMusic = MyApplication.listRockMusic
                binding.imgCoverImage.setImageResource(R.drawable.img_cover_rock_music)
                binding.tvTitle.text = resources.getString(R.string.rock)
            }
        }
    }

    private val mReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            wasRegistered = true
            val progress = intent.extras?.getInt(Constants.MUSIC_PROGRESS)
            val actionMusic = intent.extras?.getInt(Constants.MUSIC_ACTION)
            val musicPlaying = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    intent.extras?.getParcelable(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY, MusicModel.Music::class.java)
                else
                    intent.extras?.getParcelable(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY)
            runOnUiThread {
                if (!isSeeking){
                    if (progress != 0){
                        binding.tvStart.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(progress?.toLong() ?: 0L).time)
                        binding.seekbar.progress = progress ?: 0
                    }
                }
                handleAction(actionMusic, musicPlaying)
            }
        }
    }

    private fun handleAction(actionMusic: Int?, musicPlaying: MusicModel.Music?) {
        if (actionMusic != null){
            binding.btnPrevious.isEnabled = true
            binding.btnNext.isEnabled = true
            when(actionMusic){
                Constants.ACTION_RESUME -> {
                    binding.imgInteract.setImageResource(R.drawable.ic_pause)
                    rotateAnim.start()
                    isPlaying = false
                }
                Constants.ACTION_PAUSE -> {
                    binding.imgInteract.setImageResource(R.drawable.ic_play)
                    rotateAnim.cancel()
                    isPlaying = true
                }
                Constants.ACTION_PREVIOUS -> {
                    if (musicPlaying != null){
                        binding.tvEnd.text = musicPlaying.duration
                        binding.seekbar.max = Tools.convertTimeToMilliseconds(musicPlaying.duration).toInt()
                        binding.seekbar.progress = 1
                        binding.tvName.text = musicPlaying.name
                        binding.imgInteract.setImageResource(R.drawable.ic_pause)
                        rotateAnim.start()
                        binding.lavLoading.beGone()
                    }
                }
                Constants.ACTION_NEXT -> {
                    if (musicPlaying != null){
                        binding.tvEnd.text = musicPlaying.duration
                        binding.seekbar.max = Tools.convertTimeToMilliseconds(musicPlaying.duration).toInt()
                        binding.seekbar.progress = 1
                        binding.tvName.text = musicPlaying.name
                        binding.imgInteract.setImageResource(R.drawable.ic_pause)
                        rotateAnim.start()
                        binding.lavLoading.beGone()
                    }
                }
                Constants.ACTION_AUTO_NEXT -> {
                    binding.seekbar.progress = 0
                    binding.lavLoading.beVisible()
                }
                Constants.ACTION_FIRST_PLAY -> {
                    rotateAnim.start()
                    binding.lavLoading.beGone()
                }
            }
        }
    }

    private fun eventSeekbar() {
        val duration = Tools.convertTimeToMilliseconds(mMusic?.duration!!)
        binding.seekbar.max = duration.toInt()
        var numSeekTo = 0
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    numSeekTo = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (isSeeking){
                    startServiceWithProgress(numSeekTo)
                    isSeeking = false
                }
            }
        })
    }

    override fun initAction() {

    }

    override fun getContentView(): Int = R.layout.activity_music_player

    override fun bindViewModel() {

    }

    private fun eventPlay(){
        if (isPlaying){
            binding.imgInteract.setImageResource(R.drawable.ic_pause)
            rotateAnim.start()
            startServiceWithAction(Constants.ACTION_RESUME)
        }else{
            binding.imgInteract.setImageResource(R.drawable.ic_play)
            rotateAnim.cancel()
            startServiceWithAction(Constants.ACTION_PAUSE)
        }
        isPlaying = !isPlaying
    }

    private fun handlePrevious(){
        binding.btnPrevious.isEnabled = false
        startServiceWithAction(Constants.ACTION_PREVIOUS)
        binding.lavLoading.beVisible()
        rotateAnim.cancel()
    }

    private fun handleNext(){
        binding.btnNext.isEnabled = false
        startServiceWithAction(Constants.ACTION_NEXT)
        binding.lavLoading.beVisible()
        rotateAnim.cancel()
    }

    private fun handleShuffle(){
        if (sharePrefs.isShuffle){
            binding.imgShuffle.setImageResource(R.drawable.ic_shuffle)
        }else{
            binding.imgShuffle.setImageResource(R.drawable.ic_shuffle_active)
        }
        sharePrefs.isShuffle = !sharePrefs.isShuffle
        startServiceWithAction(Constants.ACTION_SHUFFLE)
    }

    private fun handleReplay(){
        if (sharePrefs.isLooping){
            binding.imgRepeatMode.setImageResource(R.drawable.ic_replay)
        }else{
            binding.imgRepeatMode.setImageResource(R.drawable.ic_repeate_active)
        }
        sharePrefs.isLooping = !sharePrefs.isLooping
        startServiceWithAction(Constants.ACTION_REPLAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        rotateAnim.cancel()
        if (wasRegistered){
            unregisterReceiver(mReceiver)
        }
    }

    private fun startServiceWithAction(action: Int){
        startService(Intent(this, MusicService::class.java).apply {
            putExtra(Constants.ACTION_MUSIC_SERVICE, action)
        })
    }

    private fun startServiceWithProgress(progress: Int){
        startService(Intent(this, MusicService::class.java).apply {
            putExtra(Constants.ACTION_SEEK_TO, progress)
        })
    }

}