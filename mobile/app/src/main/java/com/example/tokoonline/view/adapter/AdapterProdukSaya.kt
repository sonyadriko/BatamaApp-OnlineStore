package com.example.tokoonline.view.adapter

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ItemProdukSayaBinding
import com.example.tokoonline.view.activity.DetailProductActivity
import com.example.tokoonline.view.activity.ProdukSayaActivity
import com.example.tokoonline.view.activity.TambahProdukActivity

class AdapterItemProdukSaya(
    private val activity: ProdukSayaActivity,
) : RecyclerView.Adapter<AdapterItemProdukSaya.ViewHolder>() {

    private val produkSayaList = mutableListOf<Produk>()

    @Suppress("notifyDataSetChanged")
    fun submitList(newList: List<Produk>) {
        produkSayaList.clear()
        produkSayaList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProdukSayaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(produk: Produk) {
            Glide.with(binding.imgProduk.context)
                .load(produk.image)
                .into(binding.imgProduk)
            binding.tvNama.text = produk.nama
            binding.tvHarga.text = moneyFormatter(produk.harga)
            binding.tvStok.text = produk.stok.toString()
            binding.tvTerjual.text = produk.terjual.toString()

            binding.btnDetailProduk.setOnClickListener {
                val intent = Intent(binding.root.context, DetailProductActivity::class.java)
                intent.putExtra("extra_produk", produk as Parcelable)
                binding.root.context.startActivity(intent)
            }

            binding.btnUbahProduk.setOnClickListener {
                val intent = Intent(binding.root.context, TambahProdukActivity::class.java).apply {
                    putExtra("produk", produk as Parcelable)
                }
                binding.root.context.startActivity(intent)
            }

            binding.btnHapusProduk.setOnClickListener {
                val produkId = produk.id
                activity.deleteProduk(produkId, produk.idToko.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProdukSayaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = produkSayaList[position]
        holder.bind(produk)
    }

    override fun getItemCount(): Int {
        return produkSayaList.size
    }

}