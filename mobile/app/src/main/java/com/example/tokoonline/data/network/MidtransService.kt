package com.example.tokoonline.data.network

import com.example.tokoonline.data.model.midtrans.SnapTokenResponse
import com.example.tokoonline.data.model.midtrans.SnapTransactionDetailRequest
import com.example.tokoonline.data.model.midtrans.TransactionStatusResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MidtransService {

    private companion object {
        const val GET_STATUS_TRANSACTION = "{orderId}/status/"
        const val POST_SNAP_TOKEN = "charge/"
    }

    /**
     * ONLY use it with @MidtransApiService
     * get transaction detail such as status
     */
    @GET(GET_STATUS_TRANSACTION)
    fun getTransactionStatus(@Query("orderId") orderId: String): Call<TransactionStatusResponse>

    /**
     * ONLY use it with @MidtransProcessorService
     * get snap token from payment processor
     */
    @Headers("Content-Type: application/json")
    @POST(POST_SNAP_TOKEN)
    fun postSnapToken(
        @Body body: SnapTransactionDetailRequest
    ): Call<SnapTokenResponse>
}