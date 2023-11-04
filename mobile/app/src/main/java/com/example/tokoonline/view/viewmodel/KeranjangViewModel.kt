package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.base.BaseViewModel

class KeranjangViewModel : BaseViewModel() {
    private val _totalBelanja = MutableLiveData<Long>(0)
    val totalBelanja: LiveData<Long> get() = _totalBelanja


    fun addTotalBelanja(price: Long) {
        _totalBelanja.value = _totalBelanja.value?.plus(price)
    }

    fun removeTotalBelanja(price: Long) {
        if ((_totalBelanja.value ?: 0L).minus(price) >= 0L) {
            _totalBelanja.value = _totalBelanja.value?.minus(price)
        }
    }

}