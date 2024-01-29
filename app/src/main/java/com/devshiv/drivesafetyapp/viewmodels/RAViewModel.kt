package com.devshiv.drivesafetyapp.viewmodels

import android.app.Activity
import android.net.Uri
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
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RAViewModel @Inject constructor(
    private var repository: DataRepository,
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private var verificationId: String = ""

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dialogState = MutableStateFlow(DialogModel())
    val dialogState: StateFlow<DialogModel> = _dialogState

    private val _response = MutableStateFlow(false)
    val response: StateFlow<Boolean> = _response

    fun reportAccident(
        number: String,
        details: String,
        location: String,
        image: Uri,
    ) {
        showLoading()
        viewModelScope.launch {
            repository.reportAccident(number, location, details, image).collect { apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        Log.d(TAG, "reportAccident: ${apiState.data}")
                        dismissLoading()
                        when (apiState.data) {
                            is DocumentSnapshot -> {
                                val data = (apiState.data as DocumentSnapshot)
                                _response.value = true
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