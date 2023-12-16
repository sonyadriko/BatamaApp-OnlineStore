package com.example.tokoonline.data.repository.midtrans

import com.example.tokoonline.core.base.MidtransApiService
import com.example.tokoonline.core.base.MidtransProcessorService
import com.example.tokoonline.core.util.Result
import com.example.tokoonline.data.model.midtrans.SnapTokenResponse
import com.example.tokoonline.data.model.midtrans.SnapTransactionDetailRequest
import com.example.tokoonline.data.model.midtrans.TransactionStatusResponse
import com.example.tokoonline.data.network.MidtransService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.awaitResponse
import javax.inject.Inject

interface MidtransRepository {

    /**
     * ONLY use it with @MidtransApiService
     * get transaction detail such as status
     */
    fun getTransactionStatus(orderId: String): Flow<Result<TransactionStatusResponse>>

    /**
     * ONLY use it with @MidtransProcessorService
     * get snap token from payment processor
     */
    fun postSnapToken(request: SnapTransactionDetailRequest): Flow<Result<SnapTokenResponse>>
}

class MidtransRepositoryImpl @Inject constructor(
    @MidtransApiService private val midtransService: MidtransService,
    @MidtransProcessorService private val midtransProcessorService: MidtransService,
) : MidtransRepository {
    override fun getTransactionStatus(orderId: String): Flow<Result<TransactionStatusResponse>> =
        flow {
            emit(Result.Loading)
            val result = midtransService.getTransactionStatus(orderId).awaitResponse()
            if (result.isSuccessful && result.body() != null) {
                emit(Result.Success(data = result.body()!!))
            } else throw HttpException(result)
        }.catch {
            emit(Result.Error(it))
        }.flowOn(Dispatchers.IO)

    override fun postSnapToken(request: SnapTransactionDetailRequest): Flow<Result<SnapTokenResponse>> =
        flow {
            emit(Result.Loading)
            val result = midtransService.postSnapToken(request).awaitResponse()
            if (result.isSuccessful) {
                val data = requireNotNull(result.body())
                emit(Result.Success(data))
            } else throw HttpException(result)
        }.catch {
            emit(Result.Error(it))
        }.flowOn(Dispatchers.IO)
}