package com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentStatisticBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.CaloFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.DistanceFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.DurationFr
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import javax.inject.Inject

class StatisticFr @Inject constructor() : AbsFragment<FragmentStatisticBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_statistic
    }

    override fun initAction() {
    }

    override fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        binding.apply {
            if (viewPager.adapter == null) {
                viewPager.isUserInputEnabled = true
                viewPager.isSaveEnabled = false
                viewPager.offscreenPageLimit = 2
                viewPager.adapter = object : FragmentStateAdapter(requireActivity()) {

                    override fun getItemCount(): Int {
                        return 3
                    }

                    override fun createFragment(position: Int): Fragment {
                        return when (position) {
                            0 -> DistanceFr()
                            1 -> CaloFr()
                            2 -> DurationFr()
                            else -> throw IllegalStateException(
                                "ViewPage position $position"
                            )
                        }
                    }
                }
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab, position ->
                    tab.text = when (position) {
                        0 -> getString(R.string.distance)
                        1 -> getString(R.string.calories)
                        2 -> getString(R.string.duration)
                        else -> ""
                    }
                }.attach()
            }
        }
    }
}