package com.jogging.walking.weightloss.pedometer.steptracker.di

import androidx.paging.ExperimentalPagingApi
import com.jogging.walking.weightloss.pedometer.steptracker.data.api.ApiModule
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.RepositoryModule
import dagger.Module

@OptIn(ExperimentalPagingApi::class)
@Module(
    includes = [
        // module of Activity && ViewModel
        ActivityModule::class,
        ViewModelModule::class,

        // module of data
        ApiModule::class,
        RepositoryModule::class,
        //database
//        DatabaseModule::class,
    ]
)
class AppModule