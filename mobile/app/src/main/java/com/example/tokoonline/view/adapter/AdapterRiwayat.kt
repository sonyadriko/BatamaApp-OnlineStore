package com.example.tokoonline.view.adapter

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.databinding.ItemRiwayatBinding


class AdapterRiwayat(private val transactionList: List<Transaction>?) : RecyclerView.Adapter<AdapterRiwayat.ViewHolder>() {
    class ViewHolder(private val binding: ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.tvTotalHarga.text = moneyFormatter(transaction.harga.toLong())
            val produkRepository = ProdukRepository.getInstance()
            produkRepository.getProdukById(transaction.produkId){produk ->
                if (produk != null){
                    binding.tvNama.text = produk.nama
                    Glide.with(binding.imgProduk.context)
                        .load(produk.image)
                        .into(binding.imgProduk)
                }
            }


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRiwayat.ViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterRiwayat.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterRiwayat.ViewHolder, position: Int) {
        val transaction = transactionList?.get(position)

        transaction?.let {
            holder.bind(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactionList?.size ?: 0
    }

}