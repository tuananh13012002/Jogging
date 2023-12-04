package com.jogging.walking.weightloss.pedometer.steptracker.ui.plan

import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentPlanBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.PlanDetailsAct
import javax.inject.Inject

class PlanFr @Inject constructor() : AbsFragment<FragmentPlanBinding>() {
    override fun getLayoutRes() = R.layout.fragment_plan

    override fun initAction() {
        binding.plan1.onClickListener {
            startActivity(PlanDetailsAct.getIntent(requireContext(), 1))
        }
        binding.plan2.onClickListener {
            startActivity(PlanDetailsAct.getIntent(requireContext(), 2))
        }
        binding.plan3.onClickListener {
            startActivity(PlanDetailsAct.getIntent(requireContext(), 3))
        }
        binding.plan4.onClickListener {
            startActivity(PlanDetailsAct.getIntent(requireContext(), 4))
        }
    }

    override fun initView() {
        binding.txtStart.isSelected = true
        binding.txt5k.isSelected = true
        binding.txt5kTo10k.isSelected = true
        binding.txtWalking.isSelected = true
    }
}