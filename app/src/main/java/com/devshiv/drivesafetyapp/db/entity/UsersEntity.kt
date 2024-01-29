package com.devshiv.drivesafetyapp.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_db")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 1,
    @ColumnInfo(name = "phone_number")
    var phone_number: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "age")
    var age: String = "",
    @ColumnInfo(name = "vehicle")
    var vehicle: String = "",
    @ColumnInfo(name = "created")
    var created: String = "",
)
