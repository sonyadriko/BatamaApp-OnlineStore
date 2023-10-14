package com.example.tokoonline.view.viewmodel

import com.bumptech.glide.load.data.DataFetcher
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.data.repository.UserRepository


class AlamatViewModel : BaseViewModel() {

    fun addAlamat(alamat: Alamat, onComplete: (isSuccess: Boolean) -> Unit) {
        alamatRepository.pushAlamat(alamat) { isSuccess ->
            onComplete(isSuccess)
        }
    }

    fun fetchAlamatList(userRepository: UserRepository, onComplete: (List<Alamat>) -> Unit) {
        alamatRepository.getAlamatList(userRepository) { alamatList ->
            onComplete(alamatList)
        }
    }
    fun updateAlamat(alamat: Alamat, onComplete: (Boolean) -> Unit) {
        alamatRepository.updateAlamat(alamat) { isSuccess ->
            onComplete(isSuccess)
        }
    }

    fun getAlamatById(id: String, onComplete: (Alamat?) -> Unit) {
        alamatRepository.getAlamatById(id) { alamat ->
            onComplete(alamat)
        }
    }

    fun deleteAlamatById(id: String, onComplete: (Boolean) -> Unit) {
        alamatRepository.deleteAlamatById(id) { isSuccess ->
            onComplete(isSuccess)
        }
    }

}
