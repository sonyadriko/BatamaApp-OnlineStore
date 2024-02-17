package com.example.tokoonline.data.model.firebase


import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    var id: String = "",
    val orderId: String = "",
    val harga: Double = 0.0,
    val status: String = "",
    val produkId: String = "",
    val userId: String = "",
    val alamatId : String = "",
    val catatan: String = "",
    val metodePengiriman: String = "",
    val metodePembayaran: String = "",
    val snapToken: String = "",
    val createdAt: String = "",
    val idSeller: String = "",
    val terbayar: Boolean = false
) : Parcelable {

    val listOfProdukKeranjang = mutableListOf<ProdukKeranjang>()
    fun setProdukKeranjang(newList: List<ProdukKeranjang>) {
        listOfProdukKeranjang.addAll(newList)
    }

    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}