package com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2

import androidx.paging.ExperimentalPagingApi
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.BmiFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.home.HomeFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.HiphopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.PopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.RockMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan.PlanFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.setting.SettingFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.StatisticFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.CaloFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.DistanceFr
import com.jogging.walking.weightloss.pedometer.steptracker.ui.statistic.fragment.DurationFr
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module
abstract class MainV2Module {
    @ContributesAndroidInjector
    abstract fun contributeHomeFr(): HomeFr

    @ContributesAndroidInjector
    abstract fun contributePlanFr(): PlanFr

    @ContributesAndroidInjector
    abstract fun contributeBmiFr(): BmiFr

    @ContributesAndroidInjector
    abstract fun contributeSettingFr(): SettingFr

    @ContributesAndroidInjector
    abstract fun contributeStatisticFr(): StatisticFr

    @ContributesAndroidInjector
    abstract fun contributeDistanceFr(): DistanceFr

    @ContributesAndroidInjector
    abstract fun contributeDurationFr(): DurationFr

    @ContributesAndroidInjector
    abstract fun contributeCaloFr(): CaloFr

    @ContributesAndroidInjector
    abstract fun contributePopFragment(): PopMusicFragment

    @ContributesAndroidInjector
    abstract fun contributeHiphopFragment(): HiphopMusicFragment

    @ContributesAndroidInjector
    abstract fun contributeRockFragment(): RockMusicFragment

}