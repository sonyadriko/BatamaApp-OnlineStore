package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.base.BaseViewModel

class DetailProdukViewModel : BaseViewModel() {
    private val _quantity = MutableLiveData(1) // Initialize with default value
    val quantity: LiveData<Int> get() = _quantity

    // Function to update the quantity
    fun updateQuantity(newQuantity: Int) {
        _quantity.value =  newQuantity
    }
}