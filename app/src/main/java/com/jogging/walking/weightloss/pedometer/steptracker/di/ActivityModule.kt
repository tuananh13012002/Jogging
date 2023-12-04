package com.jogging.walking.weightloss.pedometer.steptracker.di

import androidx.paging.ExperimentalPagingApi
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.DetailsAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.ExploreAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.ExploreModule
import com.jogging.walking.weightloss.pedometer.steptracker.ui.intro.IntroAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main.MainActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainV2Module
import com.jogging.walking.weightloss.pedometer.steptracker.ui.multi_lang.MultiLangAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.permission.PermissionAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.ProfileAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.share.ShareAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity.MusicActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity.MusicPlayerActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.module.MusicModule
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.PlanDetailsAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.plan_details.week.WeekAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.splash.SplashActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.TrackingAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.unit.UnitActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@ExperimentalPagingApi
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMultiLangAct(): MultiLangAct

    @ContributesAndroidInjector
    abstract fun contributeIntroAct(): IntroAct

    @ContributesAndroidInjector
    abstract fun contributePermissionAct(): PermissionAct

    @ContributesAndroidInjector(modules = [MainV2Module::class])
    abstract fun contributeMainActivityV2(): MainActivityV2

    @ContributesAndroidInjector
    abstract fun contributeProfileAct(): ProfileAct

    @ContributesAndroidInjector
    abstract fun contributeTrackingAct(): TrackingAct

    @ContributesAndroidInjector
    abstract fun contributeUnitAct(): UnitActivity

    @ContributesAndroidInjector(modules = [ExploreModule::class])
    abstract fun contributeExploreActivity(): ExploreAct

    @ContributesAndroidInjector
    abstract fun contributeDetailsAct(): DetailsAct

    @ContributesAndroidInjector
    abstract fun contributeShareAct(): ShareAct

    @ContributesAndroidInjector
    abstract fun contributePlanDetailsAct(): PlanDetailsAct

    @ContributesAndroidInjector
    abstract fun contributeWeekAct(): WeekAct

    @ContributesAndroidInjector(modules = [MusicModule::class])
    abstract fun contributeMusicActivity(): MusicActivity
    @ContributesAndroidInjector
    abstract fun contributeMusicPlayerActivity(): MusicPlayerActivity

}