package com.jogging.walking.weightloss.pedometer.steptracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(ProfileViewModel::class)
//    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}