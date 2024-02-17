package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ItemDetailPesananBinding

class AdapterItemDetailPesanan : RecyclerView.Adapter<AdapterItemDetailPesanan.VH>() {
    class VH(val binding: ItemDetailPesananBinding) : RecyclerView.ViewHolder(binding.root)

    private val list = mutableListOf<ProdukKeranjang>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<ProdukKeranjang>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VH(binding = ItemDetailPesananBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvNama.text = item.nama
            tvCount.text = "${item.qty} item${if (item.qty > 1) "s" else ""}"
            tvTotalHarga.text = moneyFormatter(item.harga * item.qty)
            tvWeight.text = "${item.beratProduk} Kg"
            Glide.with(imgProduk.context)
                .load(item.image)
                .into(imgProduk)
        }
    }
}