package com.devshiv.drivesafetyapp.viewmodels

import androidx.lifecycle.ViewModel
import com.devshiv.drivesafetyapp.App
import com.devshiv.drivesafetyapp.db.dao.UsersDao
import com.devshiv.drivesafetyapp.db.entity.UsersEntity
import com.devshiv.drivesafetyapp.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    fun checkUserLoginStatus(): Flow<Boolean> = flow {
        val users: List<UsersEntity> = usersDao.getAllData()
        val value = users.isNotEmpty() && users[0].phone_number.isNotBlank()
        if (value) App.curUser = users[0].phone_number
        emit(value)
    }.flowOn(Dispatchers.IO)

}