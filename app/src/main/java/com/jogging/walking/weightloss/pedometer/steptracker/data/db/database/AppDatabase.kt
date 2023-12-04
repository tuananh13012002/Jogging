package com.jogging.walking.weightloss.pedometer.steptracker.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter.DetailsConverter
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter.FloatListConverter
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter.RunningListConverter
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter.StringListConverter
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao.RunDao
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao.UserDao
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Details
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Plan
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User


@TypeConverters(
    RunningListConverter::class,
    StringListConverter::class,
    FloatListConverter::class,
    DetailsConverter::class
)
@Database(
    entities = [User::class, Running::class, Details::class, Plan::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun runDao(): RunDao

    companion object {
        private const val DATABASE_NAME = "jogging_database"

        // Define a migration from version 1 to version 2
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define the SQL statements to perform the migration.
                // For example, you can add or modify tables here.
                // Refer to the Room documentation for details on writing migrations.
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2) // Add your migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}