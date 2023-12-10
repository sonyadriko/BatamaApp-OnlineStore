package com.example.tokoonline.data.model.firebase

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import java.io.Serializable



// NOT YET TO BE IMPLEMENTED
@Parcelize
data class Transaksi (
    var id: String = "",
    var totalharga: Long = 0,
    val qty : Int = 0,
    val idUser: String? = "",
    val idAlamat : String? = "",
    val idProduk : String? = "",
    var status: String = "Diproses",
) : Parcelable, Serializable {

    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}