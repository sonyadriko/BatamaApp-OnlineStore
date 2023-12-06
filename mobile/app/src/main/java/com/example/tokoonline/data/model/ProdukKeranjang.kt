package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * should've been only id and qty
 */
@Parcelize
data class ProdukKeranjang (
    var id: String = "",
    val image: String = "",
    var nama: String = "",
    var harga: Long = 0,
    val deskripsi: String = "",
    val beratProduk: Double = 0.0,
    val idSeller: String? = "",
    val qty: Int = 1
) : Serializable, Parcelable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}