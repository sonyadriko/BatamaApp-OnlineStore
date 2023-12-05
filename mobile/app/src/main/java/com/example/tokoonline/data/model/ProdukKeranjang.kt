package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * should've been only id and qty
 */
@Parcelize
data class ProdukKeranjang (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("nama")
    var nama: String = "",
    @SerializedName("harga")
    var harga: Long = 0,
    @SerializedName("deskripsi")
    val deskripsi: String = "",
    @SerializedName("berat_produk")
    val berat_produk: Double = 0.0,
    @SerializedName("stok")
    val stok: Int = 1,
    @SerializedName("id_users")
    val id_users: String? = "",
    @SerializedName("jumlah")
    val jumlah: Int = 1
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}