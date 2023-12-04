package com.jogging.walking.weightloss.pedometer.steptracker.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import dagger.android.support.DaggerFragment

abstract class AbsFragment<DB: ViewDataBinding> : DaggerFragment() {
    lateinit var binding: DB
    protected lateinit var sharePrefs: SharePrefs
    override fun onAttach(context: Context) {
        super.onAttach(context)    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharePrefs = SharePrefs(requireContext())
        initAction()
        initView()
    }
    abstract fun getLayoutRes(): Int
    abstract fun initAction()
    abstract fun initView()
    override fun onDetach() {
        super.onDetach()
    }
}