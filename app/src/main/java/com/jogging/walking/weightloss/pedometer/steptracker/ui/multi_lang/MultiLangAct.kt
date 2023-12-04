package com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityMultilangBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.intro.IntroAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang.adapter.MultiLangAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import javax.inject.Inject

class MultiLangAct @Inject constructor() : AbsActivity<ActivityMultilangBinding>() {
    private var type: Int? = null

    override fun initView() {
        binding.rcvLangs.adapter = MultiLangAdapter(
            MultiLangAdapter.dummyData,
            this,
            getPosition(),
        ) { _, item ->
            SystemUtil.setPreLanguage(this, item.code)
            SystemUtil.setLocale(this)
        }
    }

    override fun initAction() {
        type = intent.getIntExtra(TYPE_LANG, 0)
        when (type) {
            1 -> {
                binding.imgBack.beGone()
                binding.btnChooseLang.beVisible()
                binding.tvTitleLanguage.gravity = Gravity.LEFT

                if(!sharePrefs.isMultiLang){
                    startActivity(Intent(this, IntroAct::class.java))
                    finish()
                }

                binding.btnChooseLang.onClickListener {
                    sharePrefs.isMultiLang = false
                    startActivity(Intent(this, IntroAct::class.java))
                    finish()
                }
            }

            2 -> {
                binding.toolBar.setBackgroundResource(R.drawable.bg_toolbar_multi)
                binding.tvTitleLanguage.setTextColor(Color.WHITE)
                binding.tvTitleLanguage.gravity = Gravity.CENTER
                binding.btnChooseLang.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
                binding.imgBack.beVisible()
                binding.btnChooseLang.beGone()
                binding.imgBack.onClickListener {
                    startActivity(Intent(this, MainActivityV2::class.java))
                    finish()
                }
            }
        }
    }

    override fun getContentView() = R.layout.activity_multilang

    override fun bindViewModel() {
    }

    companion object {
        private const val TYPE_LANG = "MultiLangAct_Lang"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, MultiLangAct::class.java)
            intent.putExtra(TYPE_LANG, type)
            return intent
        }
    }

    private fun getPosition(): Int {
        return sharePrefs.positionLang
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivityV2::class.java)
        intent.putExtra("back_setting", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}