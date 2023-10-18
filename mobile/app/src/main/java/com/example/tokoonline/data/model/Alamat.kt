package com.example.tokoonline.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
data class Alamat (
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("alamat")
    val alamat: String = "",
    @SerializedName("catatan")
    val catatan: String = "",
    @SerializedName("nama")
    val nama: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("id_users")
    val id_users: String? = "",
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}