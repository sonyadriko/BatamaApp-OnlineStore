package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.tokoonline.core.base.BaseViewModel

class ProdukViewModel : BaseViewModel() {
    private val onLoadProduk = MutableLiveData<Unit>()

    val produk = Transformations.switchMap(onLoadProduk){
        produkRepository.loadProduk()
    }

    fun loadProduk(){
        onLoadProduk.value = Unit
    }

}