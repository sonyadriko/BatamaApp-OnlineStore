package com.example.tokoonline.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tokoonline.core.base.BaseViewModel
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel : BaseViewModel() {

    fun getTransaction(userUid: String, onComplete: (List<Transaction>) -> Unit) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByUserId(userUid){ transactionList ->
                val nonNullAlamatList = transactionList.filterNotNull()
                onComplete(nonNullAlamatList)
            }
        }
    }


}