package com.devshiv.drivesafetyapp.utils

sealed class ApiState {
    data class Success<T : Any>(val data: T) : ApiState()
    data class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
    object Empty : ApiState()
}