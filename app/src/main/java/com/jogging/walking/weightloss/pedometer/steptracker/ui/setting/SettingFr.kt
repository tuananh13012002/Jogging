package com.jogging.walking.weightloss.pedometer.steptracker.ui.setting

import android.content.Intent
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentSettingBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang.MultiLangAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.ProfileAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.setting.dialog.DialogRating
import com.jogging.walking.weightloss.pedometer.steptracker.ui.setting.dialog.DialogVoiceNotification
import com.jogging.walking.weightloss.pedometer.steptracker.ui.unit.UnitActivity
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import javax.inject.Inject

class SettingFr @Inject constructor(): AbsFragment<FragmentSettingBinding>() {

    private var dialogVoiceNotification:DialogVoiceNotification?=null

    private var manager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_setting
    }

    override fun initAction() {

        binding.langSetting.setOnClickListener {
            startMultiLanguage()
        }
        binding.rateSetting.setOnClickListener {
            if (restorePrefData() == true) {
                Toast.makeText(requireContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT).show()
            } else {
                showDialogRate()
            }
        }
        binding.privacySetting.setOnClickListener {
            startPolicy()
        }
        binding.profileSetting.setOnClickListener {
            startActivity(ProfileAct.getIntent(requireContext(),2))
        }
        binding.unitSetting.setOnClickListener {
            startUnit()
        }
        binding.switchNoti.isChecked = sharePrefs.isVoice
        binding.switchNoti.setOnCheckedChangeListener { _, isChecked ->
            sharePrefs.isVoice = isChecked
            showDialogNotification(isChecked)
        }
    }
    private fun showDialogNotification(isChecked:Boolean){
        if(isChecked) dialogVoiceNotification?.show()
        else dialogVoiceNotification?.dismiss()
    }

    override fun initView() {
        dialogVoiceNotification= DialogVoiceNotification(requireContext())
    }
    private fun startMultiLanguage() {
        startActivity(MultiLangAct.getIntent(requireContext(), 2))
    }

    private fun startUnit() {
        val intent = Intent(requireContext(), UnitActivity::class.java)
        startActivity(intent)
    }

    private fun startPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.POLICY_URL))
        startActivity(intent)
    }

    private fun showDialogRate() {
        val ratingDialog = DialogRating(requireContext())
        ratingDialog.init(
            requireContext(),
            object : DialogRating.OnPress {
                override fun sendThank() {
                    Toast.makeText(requireContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT)
                        .show()
                    ratingDialog.dismiss()
                }

                override fun rating() {
                    manager = ReviewManagerFactory.create(requireContext())
                    val request: Task<ReviewInfo> = manager!!.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reviewInfo = task.result
                            val flow: Task<Void> =
                                manager!!.launchReviewFlow(activity!!, reviewInfo!!)
                            flow.addOnSuccessListener {
                                ratingDialog.dismiss()

                            }
                        } else {
                            ratingDialog.dismiss()
                        }
                    }
                }

                override fun cancel() {}
                override fun later() {
                    ratingDialog.dismiss()
                }
            }
        )
        try {
            ratingDialog.show()
        } catch (e: WindowManager.BadTokenException) {
            e.printStackTrace()
        }
    }

    private fun restorePrefData(): Boolean? {
        val pref = activity?.getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        return pref?.getBoolean("isRate", false)
    }
}