package com.example.tokoonline.data.model.firebase

import java.io.Serializable
class Riwayat : Serializable{
    lateinit var nama : String
    lateinit var totalHarga : String
    var jumlah : Int = 1
    lateinit var status : String
}