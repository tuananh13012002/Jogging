//package com.example.hahalolofake.data.db.database
//
//import com.example.hahalolofake.MyApplication
//import com.example.hahalolofake.data.db.dao.UserDao
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton
//
//@Module
//class DatabaseModule {
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(application: MyApplication): AppDatabase {
//        return AppDatabase.getDatabase(application)
//    }
//
//    @Provides
//    @Singleton
//    fun provideCustomerDao(appDatabase: AppDatabase): UserDao {
//        return appDatabase.userDao()
//    }
//}