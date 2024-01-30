package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.databinding.ItemRiwayatBinding


class AdapterRiwayat(
    private val transactionList: List<Transaction>?,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<AdapterRiwayat.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction, position: Int) = with(binding) {
            tvTotalHarga.text = moneyFormatter(transaction.harga.toLong())
            labelStatus.setStatus(status = transaction.status)
            tvItem.text = "${transaction.jumlah} item${if (transaction.jumlah > 1) "s" else ""}"
            val produkRepository = ProdukRepository.getInstance()
            produkRepository.getProdukById(transaction.produkId) { produk ->
                if (produk != null) {
                    tvNama.text = produk.nama
                    Glide.with(imgProduk.context)
                        .load(produk.image)
                        .into(imgProduk)
                }
            }

            root.setOnClickListener {
                onItemClick.onClick(transaction, position)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList?.get(position)

        transaction?.let {
            holder.bind(transaction, position)
        }
    }

    override fun getItemCount(): Int {
        return transactionList?.size ?: 0
    }

}