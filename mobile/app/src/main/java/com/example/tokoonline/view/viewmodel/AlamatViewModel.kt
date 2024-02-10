package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.firebase.Alamat
import kotlinx.coroutines.launch

class AlamatViewModel : BaseViewModel() {

    fun addAlamat(alamat: Alamat, userUid: String, onComplete: (Boolean) -> Unit) {
        alamatRepository.pushAlamat(alamat, userUid) { isSuccess ->
            onComplete(isSuccess)
        }
    }

    fun getAlamat(userUid: String, onComplete: (List<Alamat>) -> Unit) {
        viewModelScope.launch {
            alamatRepository.getAlamat(userUid).collect { alamatList ->
                val nonNullAlamatList = alamatList.filterNotNull()
                onComplete(nonNullAlamatList)
            }
        }
    }

    fun updateAlamat(userUid: String, alamat: Alamat, onComplete: (Boolean) -> Unit) {
        alamatRepository.updateAlamat(userUid, alamat, onComplete)
    }

    fun getAlamatById(id: String,userUid: String,  onComplete: (Alamat?) -> Unit) {
        viewModelScope.launch {
            alamatRepository.getAlamatById(id, userUid) { alamat ->
                onComplete(alamat)
            }
        }
    }

    fun getAlamatDefault(
        userUid: String,
        onComplete: (Alamat?) -> Unit){
        viewModelScope.launch {
            alamatRepository.getAlamatByDefault(userUid) { alamat ->
                onComplete(alamat)
            }
        }
    }

    fun setDefaultAlamat(id: String, userUid: String, onComplete: (Boolean) -> Unit) {
        alamatRepository.setDefaultAlamat(id, userUid) { isSuccess ->
            onComplete(isSuccess)
        }
    }

    fun deleteAlamatById( id: String, userUid: String, onComplete: (Boolean) -> Unit) {
        alamatRepository.deleteAlamatById(id, userUid) { isSuccess ->
            onComplete(isSuccess)
        }
    }
}
