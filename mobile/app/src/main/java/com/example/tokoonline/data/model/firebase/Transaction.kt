package com.example.tokoonline.data.model.firebase

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    var id: String = "",
    val orderId: String,
    val nama: String,
    val harga: Double,
    var jumlah: Int = 1,
    val status: String,
    val produkId: String,
    val userId: String,
    val catatan: String,
    val metodePengiriman: String,
    val metodePembayaran: String
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}