package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.databinding.ItemProdukBinding


class Holder(val binding: ItemProdukBinding) : RecyclerView.ViewHolder(binding.root)
class AdapterProduk(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<Holder>() {

    private val produkList = ArrayList<Produk>()

    fun submitList(data: MutableList<Produk>) {
        produkList.clear()
        produkList.addAll(data)
        notifyItemRangeChanged(0, data.size - 1)
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
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(imgProduk)

            root.setOnClickListener {
                onItemClickListener.onItemClick(currentItem, position)
            }
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