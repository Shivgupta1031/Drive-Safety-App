package com.devshiv.drivesafetyapp.model

data class DialogModel(
    var showDialog: Boolean = false,
    var showToast: Boolean = false,
    var success: Boolean = false,
    var title: String = "",
    var description: String = ""
)