package com.example.tokoonline.core.base

import androidx.lifecycle.ViewModel
import com.example.tokoonline.data.repository.firebase.AlamatRepository
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.data.repository.firebase.TokoRepository
import com.example.tokoonline.data.repository.firebase.TransactionRepository

abstract class BaseViewModel: ViewModel() {
    val produkRepository : ProdukRepository = ProdukRepository.getInstance()
    val alamatRepository : AlamatRepository = AlamatRepository.getInstance()
    val tokoRepository : TokoRepository = TokoRepository.getInstance()
    val transactionRepository : TransactionRepository = TransactionRepository.getInstance()
}