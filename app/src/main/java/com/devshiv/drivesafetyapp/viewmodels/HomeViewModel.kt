package com.devshiv.drivesafetyapp.viewmodels

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.drivesafetyapp.App
import com.devshiv.drivesafetyapp.db.dao.UsersDao
import com.devshiv.drivesafetyapp.db.entity.UsersEntity
import com.devshiv.drivesafetyapp.model.DialogModel
import com.devshiv.drivesafetyapp.repository.DataRepository
import com.devshiv.drivesafetyapp.utils.ApiState
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.Constants.TAG
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.Result

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _userData = MutableStateFlow(UsersEntity())
    val userData: StateFlow<UsersEntity> get() = _userData

    init {
        getAllData()
    }

    fun getAllData() {
        viewModelScope.launch {
            _userData.value = withContext(Dispatchers.IO) {
                usersDao.findDataById("1")
            }
        }
    }
}