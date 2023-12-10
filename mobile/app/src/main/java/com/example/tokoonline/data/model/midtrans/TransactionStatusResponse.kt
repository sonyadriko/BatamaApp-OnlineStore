package com.example.tokoonline.data.model.midtrans

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class TransactionStatusResponse(
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("transaction_id")
    val transactionId: String,
    @SerializedName("fraud_status")
    val fraudStatus: String,
    @SerializedName("approval_code")
    val approvalCode: String,
    @SerializedName("transaction_status")
    val transactionStatus: String,
    @SerializedName("status_code")
    val statusCode: String,
    @SerializedName("reference_id")
    val referenceId: String,
    @SerializedName("signature_key")
    val signatureKey: String,
    @SerializedName("payment_option_type")
    val paymentOptionType: String,
    @SerializedName("gross_amount")
    val grossAmount: String,
    @SerializedName("card_type")
    val cardType: String,
    @SerializedName("shopeepay_reference_number")
    val shopeePayReferenceNumber: String,
    @SerializedName("payment_type")
    val paymentType: String,
    @SerializedName("bank")
    val bank: String,
    @SerializedName("masked_card")
    val maskedCard: String,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("channel_response_code")
    val channelResponseCode: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("channel_response_message")
    val channelResponseMessage: String
) : Parcelable
