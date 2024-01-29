package com.devshiv.drivesafetyapp.viewmodels

import android.app.Activity
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.Result

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dialogState = MutableStateFlow(DialogModel())
    val dialogState: StateFlow<DialogModel> = _dialogState

    private val _responseState = MutableStateFlow(false)
    val responseState: StateFlow<Boolean> = _responseState

    fun createAccount(
        phoneNumber: String,
        name: String,
        age: String,
        vehicle: String
    ) {
        showLoading()
        viewModelScope.launch {
            repository.createUser(phoneNumber, name, age, vehicle).collect { apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        dismissLoading()
                        when (apiState.data) {
                            is DocumentSnapshot -> {
                                val data = (apiState.data as DocumentSnapshot)
                                App.curUser = data.getString(Constants.PHONE_NUMBER_TAG)!!
                                saveData(
                                    UsersEntity(
                                        1,
                                        data.getString(Constants.PHONE_NUMBER_TAG)!!,
                                        data.getString(Constants.NAME_TAG)
                                            ?: "",
                                        data.getString(Constants.AGE_TAG)
                                            ?: "",
                                        data.getString(Constants.VEHICLE_TAG)
                                            ?: "",
                                        data.getTimestamp(Constants.CREATED_TAG)
                                            .toString()
                                    )
                                )
                                _responseState.value = true
                            }

                            else -> {
                                _dialogState.value = DialogModel(
                                    showDialog = true,
                                    showToast = false,
                                    success = false,
                                    title = "Failed",
                                    description = "Some Error Occurred"
                                )
                            }
                        }
                    }

                    is ApiState.Failure -> {
                        dismissLoading()
                        _dialogState.value = DialogModel(
                            showDialog = true,
                            showToast = false,
                            success = false,
                            title = "Failed",
                            description = apiState.msg.message ?: "Error Occurred"
                        )
                    }

                    ApiState.Loading -> {
                        showLoading()
                    }

                    ApiState.Empty -> {

                    }
                }
            }
        }
    }

    fun validate(text: String): Boolean {
        return text.isNotEmpty()
    }

    fun saveData(data: UsersEntity) {
        viewModelScope.launch {
            usersDao.addData(data)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            usersDao.deleteAllData()
        }
    }

    fun dismissDialog() {
        _dialogState.value = DialogModel(
            showDialog = false,
            showToast = false,
            success = false,
            title = "",
            description = ""
        )
    }

    fun dismissLoading() {
        _loadingState.value = false
    }

    fun showLoading() {
        _loadingState.value = true
    }

}