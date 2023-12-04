package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.data.model.Toko
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.flow.Flow
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






}