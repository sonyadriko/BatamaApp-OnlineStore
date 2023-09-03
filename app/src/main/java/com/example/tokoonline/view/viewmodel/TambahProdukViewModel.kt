package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.data.repository.ProdukRepository

class TambahProdukViewModel: ViewModel() {
    private val repository: ProdukRepository = ProdukRepository().getInstance()

    fun addData(produk: Produk, onComplete: (isSuccess: Boolean) -> Unit) {
//        if (status.isEmpty() && kelamin.isEmpty()) {
//            onComplete(false)
//            return
//        }

        repository.addProduk(produk) {
            onComplete(it)
        }
    }
}