package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Produk(
    @SerializedName("image")
    val image: String = "",
    @SerializedName("nama")
    var nama: String = "",
    @SerializedName("keyword")
    var keyword: String = "",
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
    @SerializedName("created_at")
    val created_at: String = "",
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }

    fun toProdukKeranjang(): ProdukKeranjang {
        return ProdukKeranjang(
            image = this.image,
            nama = this.nama,
            harga = this.harga,
            deskripsi = this.deskripsi,
            berat_produk = this.berat_produk,
            stok = this.stok,
            id_users = this.id_users,
        )
    }
}