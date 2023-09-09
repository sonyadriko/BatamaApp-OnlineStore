package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import java.io.Serializable

@Parcelize
data class Produk(

    @SerializedName("image")
    val image: String = "",
    @SerializedName("nama")
    var nama: String = "",
    @SerializedName("harga")
    var harga: Int = 0,
    @SerializedName("deskripsi")
    val deskripsi: String = "",
    @SerializedName("id_users")
    val id_users: String? = "",

//    lateinit var tag : String
//    lateinit var nama : String
//    lateinit var harga : String
//    lateinit var deskripsi : String
//    var gambar : Int = 0
): Serializable, Parcelable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }

}