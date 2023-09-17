package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.Produk

class ProdukViewModel : BaseViewModel() {
    data class State(
        val isLoading: Boolean = true,
        val dataProduk: List<Produk> = emptyList()
    )

    abstract class Effect {
        private var isConsumed = false

        fun consume(): Effect? {
            return if (isConsumed) null
            else {
                isConsumed = true
                this
            }
        }
    }

    object LoadDataProduk: Effect()

    private val _state = MediatorLiveData<State>()
    val state: LiveData<State> get() = _state

    init {
        setState { State(isLoading = true) }
    }

    private fun onLoadDataProduk() {
        val a = produkRepository.loadProduk()
        _state.addSource(a) {
            setState {
                copy(
                    isLoading = it.isEmpty(),
                    dataProduk = it
                )
            }
        }
    }

    private fun setState(modifier: State.() -> State) {
        _state.value = modifier(_state.value ?: State())
    }

    fun processEvent(e: Effect) {
        when (e) {
            is LoadDataProduk -> onLoadDataProduk()
        }
    }
}