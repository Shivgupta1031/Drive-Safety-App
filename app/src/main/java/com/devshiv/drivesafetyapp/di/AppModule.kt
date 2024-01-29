package com.devshiv.drivesafetyapp.di

import android.content.Context
import androidx.room.Room
import com.devshiv.drivesafetyapp.R
import com.devshiv.drivesafetyapp.db.AppDatabase
import com.devshiv.drivesafetyapp.db.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            appContext.getString(R.string.app_name).lowercase() + "_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao {
        return appDatabase.usersDao()
    }
}