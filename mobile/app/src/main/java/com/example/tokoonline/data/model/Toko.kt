package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
data class Toko (
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("nama")
    val nama: String = "",
    @SerializedName("id_alamat")
    val id_alamat: String = "",
    @SerializedName("id_users")
    val id_users: String? = "",
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}