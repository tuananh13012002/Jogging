package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore

import androidx.paging.ExperimentalPagingApi
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.BmiFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.challenges.ChallengesFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.overall.OverallFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.RecordsFr
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module
abstract class ExploreModule {

    @ContributesAndroidInjector
    abstract fun contributeBmiFr(): BmiFr

    @ContributesAndroidInjector
    abstract fun contributeOverallFr(): OverallFr

    @ContributesAndroidInjector
    abstract fun contributeRecordsFr(): RecordsFr

    @ContributesAndroidInjector
    abstract fun contributeChallengesFr(): ChallengesFr
}