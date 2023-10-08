package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.ProdukKeranjang
import com.example.tokoonline.databinding.ItemKeranjangBinding

class AdapterKeranjang(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdapterKeranjang.Holder>() {
    class Holder(val binding: ItemKeranjangBinding) : RecyclerView.ViewHolder(binding.root)

    private val data = mutableListOf<ProdukKeranjang?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newData: List<ProdukKeranjang?>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private fun updateData(count: Int ,position: Int) {
        val item = data[position] ?: return
        val copied = item.copy(jumlah = count)
        data.apply {
            set(position, copied)
            notifyItemChanged(position)
            onItemClickListener.onItemClick(copied, 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemKeranjangBinding.inflate(inflater, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position] ?: return

        val binding = holder.binding
        var count = item.jumlah

        binding.btnTambah.setOnClickListener {
            binding.tvJumlah.text = "${count++}"
            updateData(count, position)
        }

        binding.btnKurang.setOnClickListener {
            binding.tvJumlah.text = "${count--}"

            if (count <= 0) {
                data.removeAt(position)
                notifyItemRemoved(position)
                onItemClickListener.onItemClick(item, 0)
            } else updateData(count, position)
        }

        binding.btnDelete.setOnClickListener {
            notifyItemRemoved(position)
            onItemClickListener.onItemClick(item, 0)
        }

        binding.tvJumlah.text = count.toString()
        binding.tvHarga.text = "Rp. ${moneyFormatter((item.harga * count))}"

        binding.tvNama.text = item.nama
        Glide.with(binding.imgProduk)
            .load(item.image.toUri())
            .into(binding.imgProduk)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}