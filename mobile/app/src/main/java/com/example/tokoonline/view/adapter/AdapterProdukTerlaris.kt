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
import com.example.tokoonline.databinding.ItemProdukBinding
import com.example.tokoonline.databinding.ItemProdukTerlarisBinding

// Define a unique Holder class for AdapterProdukTerlaris
class HolderProdukTerlaris(val binding: ItemProdukTerlarisBinding) : RecyclerView.ViewHolder(binding.root)

class AdapterProdukTerlaris(private val onItemClickListener: OnItemClick) :
    RecyclerView.Adapter<HolderProdukTerlaris>() {

    private val produkListTerlaris = ArrayList<Produk>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<Produk>) {
        produkListTerlaris.clear()
        produkListTerlaris.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProdukTerlaris {
        val inflater = LayoutInflater.from(parent.context)
        return HolderProdukTerlaris(ItemProdukTerlarisBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HolderProdukTerlaris, position: Int) {
        val currentItem = produkListTerlaris[position]

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
        return produkListTerlaris.size
    }
}
