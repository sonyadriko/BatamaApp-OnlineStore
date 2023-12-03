package com.example.tokoonline.view.viewmodel

import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.Toko

class TokoViewModel : BaseViewModel() {

    fun addToko(toko: Toko, userUid: String, onComplete: (Boolean) -> Unit) {
        tokoRepository.pushToko(toko, userUid) { isSuccess ->
            onComplete(isSuccess)
        }
    }


}