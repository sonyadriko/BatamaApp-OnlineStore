package com.example.tokoonline.data.repository.midtrans

import com.example.tokoonline.data.model.midtrans.TransactionStatusResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MidtransRepository {
    fun getTransactionStatus(trxId: String): Flow<TransactionStatusResponse>
}

class MidtransRepositoryImpl : MidtransRepository {
    override fun getTransactionStatus(trxId: String): Flow<TransactionStatusResponse> = flow {

    }
}