package com.devshiv.drivesafetyapp.repository

import android.net.Uri
import android.util.Log
import com.devshiv.drivesafetyapp.utils.ApiState
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.Constants.TAG
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class DataRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun loginUser(userNumber: String): Flow<ApiState> = flow {
        try {
            val querySnapshot = checkIfUserExists(userNumber.replace("+91", "").replace(" ", ""))

            if (!querySnapshot.isEmpty) {
                Log.d(TAG, "loginUser: ${querySnapshot.documents}")
                Log.d(TAG, "loginUser: ${querySnapshot.size()}")
                emit(ApiState.Success(querySnapshot))
            } else {
                emit(ApiState.Success(false))
            }
        } catch (e: Exception) {
            emit(ApiState.Failure(e))
        }
    }

    private suspend fun checkIfUserExists(userNumber: String): QuerySnapshot {
        return firestore.collection(Constants.USERS_TAG)
            .whereEqualTo(Constants.PHONE_NUMBER_TAG, userNumber)
            .limit(1)
            .get()
            .await()
    }

    suspend fun createUser(
        userNumber: String,
        name: String,
        age: String,
        vehicle: String
    ): Flow<ApiState> = flow {
        try {
            val newUser = hashMapOf(
                Constants.PHONE_NUMBER_TAG to userNumber,
                Constants.NAME_TAG to name,
                Constants.AGE_TAG to age,
                Constants.VEHICLE_TAG to vehicle,
                Constants.CREATED_TAG to FieldValue.serverTimestamp()
            )

            val result = firestore.collection(Constants.USERS_TAG)
                .add(newUser)
                .await()

            val data = result.get().await()

            emit(ApiState.Success(data))
        } catch (e: Exception) {
            emit(ApiState.Failure(e))
        }
    }

    suspend fun reportAccident(
        userNumber: String,
        location: String,
        details: String,
        imageUri: Uri
    ): Flow<ApiState> = flow {
        try {
            val storageRef =
                FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(imageUri).await()
            val imageUrl = uploadTask.storage.downloadUrl.await()

            // Create user data with the image URL
            val newUser = hashMapOf(
                Constants.PHONE_NUMBER_TAG to userNumber,
                Constants.IMAGE_TAG to imageUrl.toString(),
                Constants.DETAILS_TAG to details,
                Constants.LOCATION_TAG to location,
                Constants.CREATED_TAG to FieldValue.serverTimestamp()
            )

            val result = firestore.collection(Constants.REPORTS_TAG)
                .add(newUser)
                .await()

            val data = result.get().await()

            emit(ApiState.Success(data))
        } catch (e: Exception) {
            emit(ApiState.Failure(e))
        }
    }
}