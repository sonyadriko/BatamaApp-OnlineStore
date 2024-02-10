package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ItemRiwayatProdukBinding


class AdapterRiwayatProduk(
    private val context: Context,
    private val listProduk: List<ProdukKeranjang>
) : RecyclerView.Adapter<AdapterRiwayatProduk.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRiwayatProdukBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProdukKeranjang, position: Int) {
            binding.apply {
                tvTotalHarga.text = moneyFormatter(item.harga * item.qty)
                tvItem.text = "${item.qty} item${if (item.qty > 1) "s" else ""}"
                tvNama.text = item.nama
                Glide.with(imgProduk.context)
                    .load(item.image)
                    .into(imgProduk)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemRiwayatProdukBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listProduk.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProduk[position], position)
    }
}