package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.data.model.ProdukKeranjang
import com.example.tokoonline.databinding.ItemProdukTransaksiBinding

class AdapterItemTransaksi(private val produkList: List<ProdukKeranjang>?) :
    RecyclerView.Adapter<AdapterItemTransaksi.ViewHolder>() {

    class ViewHolder(private val binding: ItemProdukTransaksiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produk: ProdukKeranjang) {
            binding.tvNama.text = produk.nama
            binding.tvHarga.text = produk.harga.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProdukTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = produkList?.get(position)

        produk?.let {
            holder.bind(produk)
        }
    }

    override fun getItemCount(): Int {
        return produkList?.size ?: 0
    }
}
