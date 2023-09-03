package com.inyongtisto.tokoonline.fargment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.tokoonline.R
import com.example.tokoonline.view.adapter.AdapterProduk
import com.example.tokoonline.data.model.Produk
import com.inyongtisto.tokoonline.adapter.AdapterSlider


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment() {

    // TODO: Rename and change types of parameters

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvProdukTerlaris: RecyclerView
    lateinit var rvProdukTer: RecyclerView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlaris = view.findViewById(R.id.rv_produkterlaris)
        rvProdukTer = view.findViewById(R.id.rv_produkter)

        val arrSlider = ArrayList<Int>()
            arrSlider.add(R.drawable.slider2)
            arrSlider.add(R.drawable.slider1)
            arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvProduk.adapter = AdapterProduk(arrProduk)
        rvProduk.layoutManager = layoutManager

        rvProdukTerlaris.adapter = AdapterProduk(arrProdukTerlaris)
        rvProdukTerlaris.layoutManager = layoutManager2

        rvProdukTer.adapter = AdapterProduk(arrProdukTer)
        rvProdukTer.layoutManager = layoutManager3

        return view
    }

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
    val arrProdukTerlaris: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP 14_bs749tu"
        p1.harga = 15980000
//        p1.gambar = R.drawable.slider2

        val p2 = Produk()
        p2.nama = "Hp Envy_13_aq0019tx"
        p2.harga = 15980000
//        p2.gambar = R.drawable.slider3

        val p3 = Produk()
        p3.nama = "HP pavilion_13_an0006na"
        p3.harga = 15980000
//        p3.gambar = R.drawable.slider1

        val p4 = Produk()
        p4.nama = "Hp Envy_13_aq0019tx"
        p4.harga = 15980000
//        p4.gambar = R.drawable.slider3

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        arr.add(p4)

        return arr
    }

    val arrProdukTer: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP 14_bs749tu"
        p1.harga = 15980000
//        p1.gambar = R.drawable.slider3

        val p2 = Produk()
        p2.nama = "Hp Envy_13_aq0019tx"
        p2.harga = 15980000
//        p2.gambar = R.drawable.slider1

        val p3 = Produk()
        p3.nama = "HP pavilion_13_an0006na"
        p3.harga = 15980000
//        p3.gambar = R.drawable.slider2

        val p4 = Produk()
        p4.nama = "Hp Envy_13_aq0019tx"
        p4.harga = 15980000
//        p4.gambar = R.drawable.slider1

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        arr.add(p4)

       return arr
    }
}