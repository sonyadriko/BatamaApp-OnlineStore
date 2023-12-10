package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ItemProdukBinding


class Holder(val binding: ItemProdukBinding) : RecyclerView.ViewHolder(binding.root)
class AdapterProduk(private val onItemClickListener: OnItemClick) :
    RecyclerView.Adapter<Holder>() {

    private val produkList = ArrayList<Produk>()

    fun submitList(data: List<Produk>) {
        produkList.clear()
        produkList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(ItemProdukBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = produkList[position]

        with(holder.binding) {
            tvNama.text = currentItem.nama
            tvHarga.text = currentItem.deskripsi
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