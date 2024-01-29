package com.devshiv.drivesafetyapp.model

import com.devshiv.drivesafetyapp.db.entity.UsersEntity

data class LoginResponse(
    var status: String = "",
    var message: String = "",
    var user_details: UsersEntity?
)