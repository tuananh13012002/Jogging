package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityExploreBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.BmiFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.challenges.ChallengesFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.overall.OverallFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.RecordsFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import javax.inject.Inject

class ExploreAct @Inject constructor() : AbsActivity<ActivityExploreBinding>() {
    override fun initView() {
        checkType(intent.getIntExtra(KEY_TYPE,0))
    }

    override fun initAction() {
        binding.apply {
            ivBack.setOnClickListener {
                ivBack.beGone()
                tvTitle.text = getString(R.string.explore)
                restoreOriginalContent()
            }
            bmiCalculator.setOnClickListener {
                ivBack.beVisible()
                replaceFragment(BmiFr())
            }
//            overall.setOnClickListener {
//                ivBack.beVisible()
//                replaceFragment(OverallFr())
//            }
            records.setOnClickListener {
                ivBack.beVisible()
                replaceFragment(RecordsFr())
            }
            challenges.setOnClickListener {
                ivBack.beVisible()
                replaceFragment(ChallengesFr())
            }
        }
        binding.ivBackToHome.setOnClickListener {
            startActivity(MainActivityV2.getIntent(this,2))
            finish()
        }
    }

    private fun checkType(type: Int) {
        when (type) {
            1 -> replaceFragment(BmiFr())
            2 -> replaceFragment(ChallengesFr())
            3 -> replaceFragment(RecordsFr())
            4 -> replaceFragment(OverallFr())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.ConstraintLayout, fragment)
        fragmentTransaction.commit()
        when(fragment){
            is BmiFr-> binding.tvTitle.text = getString(R.string.bmi)
            is ChallengesFr-> binding.tvTitle.text = getString(R.string.challenges)
            is RecordsFr-> binding.tvTitle.text = getString(R.string.records)
            is OverallFr-> binding.tvTitle.text = getString(R.string.overall)
        }
    }

    private fun restoreOriginalContent() {
        val fragmentManager = supportFragmentManager
        for (fragment in fragmentManager.fragments) {
            fragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_explore
    }

    override fun bindViewModel() {
    }

    companion object {
        private const val KEY_TYPE = "ExploreAct_Type"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, ExploreAct::class.java)
            intent.putExtra(KEY_TYPE, type)
            return intent
        }
    }
}