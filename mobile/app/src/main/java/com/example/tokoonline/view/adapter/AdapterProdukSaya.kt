package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ItemProdukSayaBinding

class AdapterItemProdukSaya(
    private val produkSayaList : List<Produk>
) : RecyclerView.Adapter<AdapterItemProdukSaya.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemProdukSayaBinding) :
            RecyclerView.ViewHolder(binding.root){
                fun bind (produk: Produk){
                    Glide.with(binding.imgProduk.context)
                        .load(produk.image)
                        .into(binding.imgProduk)
                    binding.tvNama.text = produk.nama
                    binding.tvHarga.text = moneyFormatter(produk.harga)
                    binding.tvBerat.text = produk.beratProduk.toString() + " Kg"
                    binding.tvStok.text = "Stok : " + produk.stok.toString()

                    binding.btnDetailProduk.setOnClickListener{

                    }

                    binding.btnUbahProduk.setOnClickListener{

                    }

                    binding.btnHapusProduk.setOnClickListener{

                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterItemProdukSaya.ViewHolder {
        val binding = ItemProdukSayaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterItemProdukSaya.ViewHolder, position: Int) {
        val produk = produkSayaList?.get(position)

        produk?.let {
            holder.bind(produk)
        }
    }

    override fun getItemCount(): Int {
        return produkSayaList?.size ?: 0
    }




}