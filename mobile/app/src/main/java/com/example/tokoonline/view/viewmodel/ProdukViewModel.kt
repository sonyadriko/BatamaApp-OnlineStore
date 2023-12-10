package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.firebase.Produk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProdukViewModel : BaseViewModel() {

    private val supervisor = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + supervisor)

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

    object LoadDataProduk : Effect()

    private val _state = MediatorLiveData<State>()
    val state: LiveData<State> get() = _state

    init {
        setState { State(isLoading = true) }
    }

    private fun onLoadDataProduk() {
        viewModelScope.launch {
            produkRepository.loadProduk().collect { dataList ->
                setState {
                    copy(
                        isLoading = false,
                        dataProduk = dataList.filterNotNull()
                    )
                }
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

    fun loadProdukbyIDToko(tokoID: String, onComplete: (List<Produk>) -> Unit) {
        viewModelScope.launch {
            produkRepository.getProdukByTokoId(tokoID) { produkList ->
                val nonNullProdukList = produkList.filterNotNull() // Filter out null elements
                onComplete(nonNullProdukList)
            }
        }
    }

}