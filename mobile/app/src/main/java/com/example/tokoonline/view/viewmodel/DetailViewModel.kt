package com.example.tokoonline.view.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.core.constanst.Constant.NAMA_PRODUK_QUERY_PARAM
import com.example.tokoonline.data.model.firebase.Produk

class DetailViewModel : BaseViewModel() {
    companion object {
        enum class DetailFeature(val queryName: String) {
            PRODUK(NAMA_PRODUK_QUERY_PARAM),
            UNKNOWN("")
        }
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val _viewEffect = MutableLiveData<ViewEffect>()
    val viewEffect: LiveData<ViewEffect> get() = _viewEffect

    var feature: DetailFeature = DetailFeature.UNKNOWN
    var namaProduk: String? = null

    var dataProduk: Produk = Produk()
        private set

    fun initBundle(data: Uri?) {
        if (data == null) {
            setViewEffect(ViewEffect.ShowToast("Broken Link"))
            setViewState(ViewState.FAIL)
            return
        }

        try {
//            feature = valueOf(data.lastPathSegment?.uppercase() ?: "")
            namaProduk = data.getQueryParameter(feature.queryName)
            if (namaProduk.isNullOrEmpty()) throw IllegalArgumentException("Name not specified")
        } catch (e: Exception) {
            setViewEffect(ViewEffect.ShowToast("Broken Link"))
            setViewState(ViewState.FAIL)
        }
    }

    fun initBundle(newFeature: DetailFeature, newNamaHewan: String) {
        feature = newFeature
        namaProduk = newNamaHewan
    }

    private fun setViewEffect(effect: ViewEffect) {
        _viewEffect.value = effect
    }

    private fun setViewState(state: ViewState) {
        _viewState.value = state
    }
    enum class ViewState {
        INITIAL,
        LOADING,
        FAIL
    }

    sealed interface ViewEffect {
        class ShowToast(val message: String? = null) : ViewEffect
        class OnDataGetResult(val data: Any?, val qrContent: String) : ViewEffect
        class OnDataDeleted(val isSuccess: Boolean) : ViewEffect
    }
}