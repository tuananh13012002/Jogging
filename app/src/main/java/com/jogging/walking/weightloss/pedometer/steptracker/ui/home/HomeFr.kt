package com.jogging.walking.weightloss.pedometer.steptracker.ui.home

import android.annotation.SuppressLint
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentHomeBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.toDateLong
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.DetailsAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.ExploreAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.adapter.ExploreAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.adapter.HistoryAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.listener.ListenerExplore
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.listener.ListenerHistory
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.viewmodel.HomeViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan.PlanFr
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalCalories
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalDistance
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalDuration
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.formatTime
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.isPlan
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil.getPreLanguage
import javax.inject.Inject

class HomeFr @Inject constructor() : AbsFragment<FragmentHomeBinding>(), ListenerExplore ,ListenerHistory{
    private val viewModel by viewModels<HomeViewModel>()

    private val exploreAdapter by lazy {
        ExploreAdapter(requireContext(),this)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initAction() {
        binding.viewAllExplore.setOnClickListener {
            startActivity(ExploreAct.getIntent(requireContext(), 0))
        }
        binding.viewAllOverall.setOnClickListener {
            startActivity(ExploreAct.getIntent(requireContext(), 4))
        }
        binding.viewAllHistory.setOnClickListener {
            startActivity(ExploreAct.getIntent(requireContext(), 4))
        }
        binding.btnPlan.setOnClickListener {
            (activity as MainActivityV2).replaceFragment(PlanFr())
            (activity as MainActivityV2).updateBottomNavSelection(R.id.plan)
        }
    }

    override fun initView() {
        if(isPlan(requireContext())){
            binding.btnPlan.beGone()
            binding.planLayout.beVisible()
            binding.txtPlan.text=getString(R.string._10km_4_weeks)
            binding.txtPlanTitle.text=getString(R.string.start_running)
        }else{
            binding.btnPlan.beVisible()
            binding.planLayout.beGone()
            binding.txtPlan.text=getString(R.string.join_now)
            binding.txtPlanTitle.text=getString(R.string.your_goal_planing)
        }
        binding.txtCalo.isSelected = true
        binding.txtDistance.isSelected = true
        binding.txtDuration.isSelected = true
        binding.txtPlanTitle.isSelected = true
        setOnBackPressed()
        bindExplore()
        bindHistory()
    }


    private fun bindExplore() {
        binding.rvExplore.adapter = exploreAdapter
    }

    private fun bindOverall(list: MutableList<Running>) {
        val totalCalories = calculateTotalCalories(list)
        val totalDistance = calculateTotalDistance(list).div(1000f)
        val duration = DeviceUtil.getFormattedStopWatchTime(calculateTotalDuration(list))
        val time = formatTime(duration)

        binding.txtCalo.text = DeviceUtil.formattedValue(totalCalories)
        binding.txtDistance.text = String.format("%.2f", totalDistance)
        binding.txtDuration.text = time
    }

    @SuppressLint("SetTextI18n")
    private fun bindProgress(list: MutableList<Running>){
        list.takeIf { it.isNotEmpty() }?.let {
            if(isPlan(requireContext())){
                val mResult = ArrayList<String>()
                list.map {
                    if (!mResult.contains(it.duration) && DeviceUtil.convertDateFormat(it.duration ?: "",).toDateLong() >= sharePrefs.durationStart?.toDateLong()!!){
                        mResult.add(it.duration ?: "")
                    }
                }
                MyApplication.progressPlan=mResult.size * 35
                binding.progressBar.progress = mResult.size * 35
                val progressBar = (binding.progressBar.progress  / 1000f) * 100
                binding.txtProgressbar.text = String.format("%.1f", progressBar) +" %"
                DeviceUtil.animationProgress(binding.progressBar).start()
            }
        }
    }

    private fun bindHistory() {
        viewModel.getRuns.observe(viewLifecycleOwner) {
            val historyAdapter = HistoryAdapter(requireContext(), it,1,this)
            binding.rvHistory.adapter = historyAdapter
            bindOverall(it)
            bindProgress(it)
        }
    }


    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    isEnabled = false

                }
            }
        })
    }

    override fun onClickExplore(position: Int) {
        when (position) {
            0 -> startActivity(ExploreAct.getIntent(requireContext(), 1))
            1 -> startActivity(ExploreAct.getIntent(requireContext(), 2))
            2 -> startActivity(ExploreAct.getIntent(requireContext(), 3))
            3 -> startActivity(ExploreAct.getIntent(requireContext(), 4))
        }
    }

    override fun onClickDetail(id: Long) {
        startActivity(DetailsAct.getIntent(requireContext(),id,1))
    }
}
