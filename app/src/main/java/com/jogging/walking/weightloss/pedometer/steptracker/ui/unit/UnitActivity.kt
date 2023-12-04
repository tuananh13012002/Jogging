package com.jogging.walking.weightloss.pedometer.steptracker.ui.unit

import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityUnitBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.viewmodel.ProfileViewModel
import javax.inject.Inject


class UnitActivity @Inject constructor() : AbsActivity<ActivityUnitBinding>() {
    private val viewModel by viewModels<ProfileViewModel>()
    private var selectedUnit= MutableLiveData<String>()
    private var userUpdate: User?=null

    override fun getContentView(): Int {
        return R.layout.activity_unit
    }

    override fun initView() {
        selectedUnit.observe(this){ unit->
            MyApplication.UNIT=unit
        }
        viewModel.getUsers.observe(this){user->
            if(user!=null) binding.progress.root.beGone()
            checkUnit(user.units ?: "")
            userUpdate = User(user.id,user.username,user.avatar,user.age,user.height,user.weight,user.listRun,user.gender, MyApplication.UNIT)
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rdo_km -> {
                    selectedUnit.postValue("km")
                    binding.txtKm.setBackgroundResource(R.drawable.bg_border_lang)
                    binding.txtMi.setBackgroundResource(R.drawable.bg_border_lang_unselect)
                }

                R.id.rdo_mi -> {
                    selectedUnit.postValue("mi")
                    binding.txtMi.setBackgroundResource(R.drawable.bg_border_lang)
                    binding.txtKm.setBackgroundResource(R.drawable.bg_border_lang_unselect)

                }
            }
        }
    }

    override fun initAction() {
        binding.imgBack.setOnClickListener {
            userUpdate?.let { viewModel.updateUser(it) }
            finish()
        }
        binding.txtKm.setOnClickListener {
            binding.rdoKm.isChecked = true
        }
        binding.txtMi.setOnClickListener {
            binding.rdoMi.isChecked = true
        }
    }

    private fun checkUnit(unit: String) {
        when (unit) {
            "km" -> binding.rdoKm.isChecked = true
            "mi" -> binding.rdoMi.isChecked = true
        }
    }

    override fun bindViewModel() {
    }

    override fun onStop() {
        super.onStop()
        userUpdate?.let {
            viewModel.updateUser(it)
        }
    }
}