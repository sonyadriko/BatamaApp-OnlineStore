package com.example.tokoonline.data.model.firebase


import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

//data class Transaction(
//    var id: String = "",
//    val orderId: String,
//    val nama: String,
//    val harga: Double,
//    var jumlah: Int = 1,
//    val status: String,
//    val produkId: String,
//    val userId: String,
//    val catatan: String,
//    val metodePengiriman: String,
//    val metodePembayaran: String,
//    val snapToken: String
//) : Serializable {
//    // No-argument constructor is needed for Firebase
//    constructor() : this("", "", "", 0.0, 1, "", "", "", "", "", "", "")
//
//    fun toMap(): Map<String, Any?> {
//        val gson = Gson()
//        val json = gson.toJson(this)
//        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
//    }
//}

@Parcelize
data class Transaction(
    var id: String = "",
    val orderId: String = "",
    val nama: String = "",
    val harga: Double = 0.0,
    var jumlah: Int = 1,
    val status: String = "",
    val produkId: String = "",
    val userId: String = "",
    val alamatId : String = "",
    val catatan: String = "",
    val metodePengiriman: String = "",
    val metodePembayaran: String = "",
    val snapToken: String = "",
    val createdAt: String = "",
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}