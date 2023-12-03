package com.example.tokoonline.core.constanst

import com.example.tokoonline.data.model.Produk

object Constant {
    enum class Role {
        PENJUAL,
        PEMBELI,
        ADMIN
    }

    const val REFERENCE_USER = "Users"
    const val REFERENCE_PRODUK = "Produks"
    const val NAMA_PRODUK_QUERY_PARAM = "namaProduk"
    const val REFERENCE_ALAMAT = "Alamat"
    const val REFERENCE_KERANJANG = "Keranjang"
    const val REFERENCE_TOKO = "Toko"



    // DUMMY DATA
    val arrProduk: ArrayList<Produk>get() {
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "Hp Iphone dan Android baru"
        p1.harga = 15980000
//        p1.gambar = R.drawable.slider1

        val p2 = Produk()
        p2.nama = "Perabotan memasak paling murah, banyak pilihan warna, anti lengket dan bisa menggoreng tanpa minyak"
        p2.harga = 15980000
//        p2.gambar = R.drawable.slider2

        val p3 = Produk()
        p3.nama = "Perabotan memasak paling murah, anti lengket dan bisa menggoreng tanpa minyak"
        p3.harga = 15980000
//        p3.gambar = R.drawable.slider3

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)

        return arr
    }
}