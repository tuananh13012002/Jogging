package com.jogging.walking.weightloss.pedometer.steptracker.ui.main

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityMainBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main.adapter.CharacterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity @Inject constructor() : AbsActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels {
        viewModelFactory
    }

    private val characterAdapter by lazy {
        CharacterAdapter()
    }

     override fun initView() {
        binding.recycleView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.adapter = characterAdapter
         viewModel.page.value = 1
         lifecycleScope.launch {
             viewModel.results.collectLatest {
                 characterAdapter.submitData(it)
             }
         }
    }

    override fun initAction() {
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun bindViewModel() {

    }

    private fun initData() {

    }
}