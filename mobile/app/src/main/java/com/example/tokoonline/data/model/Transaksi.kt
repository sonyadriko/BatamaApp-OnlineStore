package com.example.tokoonline.data.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import java.io.Serializable



// NOT YET TO BE IMPLEMENTED
@Parcelize
data class Transaksi (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("harga")
    var totalharga: Long = 0,
    @SerializedName("totalitem")
    val totalitem : Int = 0,
    @SerializedName("id_user")
    val idUser: String? = "",
    @SerializedName("id_alamat")
    val idAlamat : String? = "",
    @SerializedName("id_produk")
    val idProduk : String? = "",
    @SerializedName("status")
    var status: String = "Diproses",
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}