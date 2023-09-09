package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.Produk
import java.text.NumberFormat
import java.util.Locale

class AdapterProduk(private val onItemClick: OnItemClick) : RecyclerView.Adapter<AdapterProduk.Holder>() {

    private val produkList = ArrayList<Produk>()

    fun submitList(data: MutableList<Produk>) {
        produkList.clear()
        produkList.addAll(data)
        notifyItemRangeChanged(0, data.size - 1)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = produkList[position]

        val harga = currentItem.harga // Misalnya, harga dalam bentuk Integer atau Double
        val localeID = Locale("id", "ID") // Locale untuk Indonesia
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        holder.tvNama.text = currentItem.nama
//        holder.tvHarga.text = currentItem.harga.toString()
        holder.tvHarga.text = formatRupiah.format(harga)
        Glide.with(holder.imgProduk)
            .load(currentItem.image)
            .placeholder(R.drawable.ic_account_circle_black_24dp)
            .into(holder.imgProduk)

        holder.itemView.setOnClickListener {
            onItemClick.onClick(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return produkList.size
    }
    fun updateUserList(userList: List<Produk>){
        this.produkList.clear()
        this.produkList.addAll(userList)
        notifyDataSetChanged()
    }
}