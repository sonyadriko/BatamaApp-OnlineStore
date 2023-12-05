package com.example.tokoonline.core.base

import androidx.lifecycle.ViewModel
import com.example.tokoonline.data.repository.AlamatRepository
import com.example.tokoonline.data.repository.ProdukRepository
import com.example.tokoonline.data.repository.TokoRepository

abstract class BaseViewModel: ViewModel() {
    val produkRepository : ProdukRepository = ProdukRepository.getInstance()
    val alamatRepository : AlamatRepository = AlamatRepository.getInstance()
    val tokoRepository : TokoRepository = TokoRepository.getInstance()
}