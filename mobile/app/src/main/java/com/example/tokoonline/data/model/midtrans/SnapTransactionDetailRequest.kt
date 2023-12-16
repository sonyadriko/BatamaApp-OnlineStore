package com.example.tokoonline.data.model.midtrans

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
@Parcelize
data class SnapTransactionDetailRequest(
	@SerializedName("gross_amount")
	val grossAmount: Long,
	@SerializedName("order_id")
	val orderId: String
) : Parcelable
