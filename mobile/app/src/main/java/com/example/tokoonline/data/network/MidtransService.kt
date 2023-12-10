package com.example.tokoonline.data.network

import com.example.tokoonline.data.model.midtrans.TransactionStatusResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MidtransService {

    private companion object {
        const val STATUS_TRANSACTION = "{orderId}/status/"
    }

    @GET(STATUS_TRANSACTION)
    fun getTransactionStatus(@Query("orderId") orderId: String): Call<TransactionStatusResponse>
}