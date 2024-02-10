package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.databinding.ItemDetailProdukCheckoutBinding

class AdapterListProduk(private val produkList: List<ProdukKeranjang>?) :
    RecyclerView.Adapter<AdapterListProduk.ViewHolder>() {
    class ViewHolder(private val binding: ItemDetailProdukCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(produk: ProdukKeranjang) = with(binding) {
            tvTotalHarga.text = moneyFormatter(produk.harga)
            tvWeight.text = "${produk.beratProduk.toFloat() * produk.qty}Kg"
            tvCount.text = "${produk.qty} item${if (produk.qty > 1) "s" else ""}"
            val produkRepository = ProdukRepository.getInstance()
            produkRepository.getProdukById(produk.produkId) { produk ->
                if (produk != null) {
                    tvNama.text = produk.nama
                    Glide.with(imgProduk.context)
                        .load(produk.image)
                        .into(imgProduk)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailProdukCheckoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = produkList?.get(position)

        transaction?.let {
            holder.bind(transaction)
        }
    }

    override fun getItemCount(): Int {
        return produkList?.size ?: 0
    }
}
