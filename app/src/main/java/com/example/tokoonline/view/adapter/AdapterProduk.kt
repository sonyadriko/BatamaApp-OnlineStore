package com.example.tokoonline.view.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.data.model.Produk

class AdapterProduk(var data:ArrayList<Produk>) : RecyclerView.Adapter<AdapterProduk.Holder>() {

    private val produkList = ArrayList<Produk>()

    class Holder(view : View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = produkList[position]

        holder.tvNama.text = currentItem.nama
        holder.tvHarga.text = currentItem.deskripsi
        Glide.with(holder.imgProduk)
            .load(currentItem.image)
            .placeholder(R.drawable.ic_account_circle_black_24dp)
            .into(holder.imgProduk)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}