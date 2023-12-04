package com.jogging.walking.weightloss.pedometer.steptracker.ui.music.module

import androidx.paging.ExperimentalPagingApi
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.HiphopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.PopMusicFragment
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.frags.RockMusicFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module
abstract class MusicModule {
    @ContributesAndroidInjector
    abstract fun bindHiphopMusicFragment(): HiphopMusicFragment

    @ContributesAndroidInjector
    abstract fun bindPopMusicFragment(): PopMusicFragment

    @ContributesAndroidInjector
    abstract fun bindRockMusicFragment(): RockMusicFragment

}