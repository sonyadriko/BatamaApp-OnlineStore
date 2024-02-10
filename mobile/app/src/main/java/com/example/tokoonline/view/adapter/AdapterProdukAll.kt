package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ItemProdukAllBinding


class HolderProdukAll(val binding: ItemProdukAllBinding) : RecyclerView.ViewHolder(binding.root)

class AdapterProdukAll(private val onItemClickListener: OnItemClick) :
    RecyclerView.Adapter<HolderProdukAll>() {

    private val produkList = ArrayList<Produk>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<Produk>) {
        produkList.clear()
        produkList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProdukAll {
        val inflater = LayoutInflater.from(parent.context)
        return HolderProdukAll(ItemProdukAllBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HolderProdukAll, position: Int) {
        val currentItem = produkList[position]

        with(holder.binding) {
            tvNama.text = currentItem.nama
            tvHarga.text = moneyFormatter(currentItem.harga)
            Glide.with(imgProduk)
                .load(currentItem.image)
                .placeholder(R.drawable.loading_animation)
                .into(imgProduk)

            root.setOnClickListener {
                onItemClickListener.onClick(currentItem, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return produkList.size
    }
}
