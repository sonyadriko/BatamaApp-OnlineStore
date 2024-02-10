package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ItemRiwayatProdukBinding


class AdapterRiwayatProduk(
    private val context: Context,
    private val listProduk: List<ProdukKeranjang>
) : BaseAdapter() {
    override fun getCount(): Int {
        return listProduk.size
    }

    override fun getItem(position: Int): ProdukKeranjang {
        return listProduk[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemRiwayatProdukBinding.inflate(layoutInflater, parent, false)
        val item = getItem(position)

        binding.apply {
            tvTotalHarga.text = moneyFormatter(item.harga * item.qty)
            tvItem.text = "${item.qty} item${if (item.qty > 1) "s" else ""}"
            tvNama.text = item.nama
            Glide.with(imgProduk.context)
                .load(item.image)
                .into(imgProduk)
        }

        return binding.root
    }
}