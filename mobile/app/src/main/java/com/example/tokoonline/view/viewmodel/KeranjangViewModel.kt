package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.ProdukKeranjang

class KeranjangViewModel : BaseViewModel() {
    private val _totalBelanja = MutableLiveData<Long>(0)
    val totalBelanja: LiveData<Long> get() = _totalBelanja

    private val _produkToBePaid = mutableListOf<ProdukKeranjang>()
    val produkToBePaid: List<ProdukKeranjang> get() = _produkToBePaid


    fun addTotalBelanja(price: Long) {
        _totalBelanja.value = _totalBelanja.value?.plus(price)
    }

    fun removeTotalBelanja(price: Long) {
        if ((_totalBelanja.value ?: 0L).minus(price) >= 0L) {
            _totalBelanja.value = _totalBelanja.value?.minus(price)
        }
    }

    fun addProdukToBePaid(data: ProdukKeranjang) {
        _produkToBePaid.add(data)
    }

    fun removeProdukToBePaid(data: ProdukKeranjang) {
        _produkToBePaid.remove(data)
    }


}