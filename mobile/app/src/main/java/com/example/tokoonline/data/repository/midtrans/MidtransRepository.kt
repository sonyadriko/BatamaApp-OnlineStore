package com.example.tokoonline.data.repository.midtrans

import com.example.tokoonline.core.util.Result
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
    fun getTransactionStatus(orderId: String): Flow<Result<TransactionStatusResponse>>
}

class MidtransRepositoryImpl @Inject constructor(
    private val midtransService: MidtransService
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
}