package com.example.tokoonline.data.model.midtrans

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SnapTokenResponse(
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("error_messages")
    val errorMessages: List<String> = emptyList(),
    @SerializedName("token")
    val token: String = ""
) : Parcelable
