package com.jogging.walking.weightloss.pedometer.steptracker.ui.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityDetailsBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.share.ShareAct
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.formattedValue
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class DetailsAct @Inject constructor() : AbsActivity<ActivityDetailsBinding>() {
    private val viewModel by viewModels<DetailsViewModel>()
    var id: Long = 0L
    override fun initView() {
        when(intent.getIntExtra(TYPE_DETAIL,0)){
            1->{
                id = intent.getLongExtra(DETAIL_RUN, 0L)
                viewModel.getRunById(id).observe(this) {
                    it?.let { running ->
                        binding.root.postDelayed({
                            binding.progress.root.beGone()
                        }, 3000)
                        bindData(running)
                    }
                }
            }
            2->{
                viewModel.getRuns.observe(this){
                     it.takeIf { it.isNotEmpty() }?.let { runs->
                         binding.root.postDelayed({
                             binding.progress.root.beGone()
                         }, 3000)
                         bindData(runs[0])
                         id=runs[0].id?:0L
                     }
                }
            }
        }
    }

    private fun bindData(running:Running){
        val time = DeviceUtil.getFormattedStopWatchTime(running.timesInMillis ?: 0L)
        binding.txtTime.text = running.timeStart + " - " + running.timeEnd
        binding.txtDuration.text = running.duration
        binding.imgMap.setImageBitmap(running.imageMap)
        binding.txtDistance.text = (running.distance?.div(1000f).toString())
        binding.txtDuration2.text = time
        binding.txtCalo.text = formattedValue(running.calo ?: 0f)
        binding.txtSpeed.text = String.format("%.1f", running.speeds ?: 0f)
        binding.txtPace.text = String.format("%.1f", DeviceUtil.calculatePaceFromSpeed(running.speeds ?: 0f))
    }

    override fun initAction() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnShare.onClickListener {
            startActivity(ShareAct.getIntent(this, id))
        }
    }

    override fun getContentView() = R.layout.activity_details

    override fun bindViewModel() {
    }

    companion object {
        private const val DETAIL_RUN = "Detail_Run"
        private const val TYPE_DETAIL = "Detail_Run_Type"
        fun getIntent(context: Context, id: Long,type:Int): Intent {
            val intent = Intent(context, DetailsAct::class.java)
            intent.putExtra(DETAIL_RUN, id)
            intent.putExtra(TYPE_DETAIL, type)
            return intent
        }
    }
}