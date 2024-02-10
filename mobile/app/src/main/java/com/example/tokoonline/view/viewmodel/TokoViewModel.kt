package com.example.tokoonline.view.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.firebase.Toko
import kotlinx.coroutines.launch

class TokoViewModel : BaseViewModel() {

    fun addToko(toko: Toko, userUid: String, onComplete: (Boolean) -> Unit) {
        tokoRepository.pushToko(toko, userUid) { isSuccess ->
            onComplete(isSuccess)
        }
    }

    fun getTokoData(
        userUid: String,
        onComplete: (Toko?) -> Unit){
        viewModelScope.launch {
            tokoRepository.getTokoData(userUid) { Toko ->
                onComplete(Toko)
            }
        }
    }

    fun getTokoById(id: String,userUid: String,  onComplete: (Toko?) -> Unit) {
        viewModelScope.launch {
            tokoRepository.getTokoById(id, userUid) { toko ->
                onComplete(toko)
            }
        }
    }

    fun updateToko( userUid: String, toko: Toko, onComplete: (Boolean) -> Unit) {
        Log.d(TAG, "Updating Toko: $toko for user: $userUid")
        viewModelScope.launch {
            tokoRepository.updateToko(userUid, toko) { isSuccess ->
                Log.d(TAG, "Update operation completed. Success: $isSuccess")
                onComplete(isSuccess)
            }
        }
    }


    fun checkUserHasToko(userId: String, callback: (Boolean) -> Unit) {
        // Assuming you have a repository method to check if the user has a toko
        tokoRepository.checkUserHasToko(userId) { userHasToko ->
            callback(userHasToko)
        }
    }






}