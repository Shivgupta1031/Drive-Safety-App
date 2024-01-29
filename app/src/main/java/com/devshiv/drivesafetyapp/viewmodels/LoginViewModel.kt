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
class LoginViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private var verificationId: String = ""

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dialogState = MutableStateFlow(DialogModel())
    val dialogState: StateFlow<DialogModel> = _dialogState

    private val _otpSentState = MutableStateFlow(false)
    val otpSentState: StateFlow<Boolean> = _otpSentState

    private val _userFoundState = MutableStateFlow(0)
    val userFoundState: StateFlow<Int> = _userFoundState

    fun requestOtp(context: Activity, phoneNumber: String) {
        showLoading()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted: ")
                    viewModelScope.launch {
                        signInWithPhoneAuthCredential(credential)
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d(TAG, "Verification failed: ${e.message}")
                    dismissLoading()
                    _dialogState.value = DialogModel(
                        showDialog = true,
                        success = false,
                        title = "OTP Failed",
                        description = e.message ?: "Failed To Send OTP",
                    )
                    _otpSentState.value = false
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@LoginViewModel.verificationId = verificationId
                    dismissLoading()
                    _dialogState.value = DialogModel(
                        showDialog = false,
                        showToast = true,
                        success = true,
                        title = "OTP Sent",
                        description = "OTP Sent Successfully"
                    )
                    _otpSentState.value = true
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String) {
        showLoading()
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        viewModelScope.launch {
            signInWithPhoneAuthCredential(credential)
        }
    }

    private suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val phoneNumber = auth.currentUser?.phoneNumber
                    viewModelScope.launch {
                        repository.loginUser(phoneNumber.toString()).collect { apiState ->
                            when (apiState) {
                                is ApiState.Success<*> -> {
                                    dismissLoading()
                                    when (apiState.data) {
                                        is QuerySnapshot -> {
                                            val data = (apiState.data as QuerySnapshot).documents[0]
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
                                            _userFoundState.value = 1
                                        }

                                        else -> {
                                            _userFoundState.value = 2
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
                } else {
                    dismissLoading()
                    _dialogState.value = DialogModel(
                        showDialog = true,
                        showToast = false,
                        success = false,
                        title = "Authentication Failed",
                        description = task.exception?.message ?: "Some Error Occurred"
                    )
                }
            }
    }

    fun validateNumber(text: String): Boolean {
        return text.length >= 10
    }

    fun validateOTP(text: String): Boolean {
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

    fun resetOtp() {
        _otpSentState.value = false
    }

}