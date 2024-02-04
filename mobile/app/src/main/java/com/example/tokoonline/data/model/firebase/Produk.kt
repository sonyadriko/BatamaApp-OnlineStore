package com.example.tokoonline.data.model.firebase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Produk(
    val id: String = "",
    val image: String = "",
    var nama: String = "",
    var keyword: String = "",
    var harga: Long = 0,
    val deskripsi: String = "",
    val beratProduk: Double = 0.0,
    val stok: Int = 1,
    val idSeller: String? = "",
    val idToko: String? = "",
    val createdAt: String = "",
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }

    fun toProdukKeranjang(jumlah: Int): ProdukKeranjang {
        return ProdukKeranjang(
            id = this.id,
            image = this.image,
            nama = this.nama,
            harga = this.harga,
            deskripsi = this.deskripsi,
            beratProduk = this.beratProduk,
            idSeller = this.idSeller,
            qty = jumlah,
            produkId = this.id,
            stok = this.stok
        )
    }
}