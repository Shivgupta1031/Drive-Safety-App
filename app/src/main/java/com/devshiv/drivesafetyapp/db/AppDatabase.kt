package com.devshiv.drivesafetyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devshiv.drivesafetyapp.db.dao.UsersDao
import com.devshiv.drivesafetyapp.db.entity.UsersEntity

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

}