package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokoonline.core.util.OnItemCheckBoxListener
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ItemKeranjangBinding

class AdapterKeranjang(
    private val onItemClickListener: OnItemClick,
    private val onItemCheckBoxListener: OnItemCheckBoxListener,
) : RecyclerView.Adapter<AdapterKeranjang.Holder>() {
    class Holder(val binding: ItemKeranjangBinding) : RecyclerView.ViewHolder(binding.root)

    private val data = mutableListOf<ItemData>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newData: List<ProdukKeranjang?>) {
        if (data.isEmpty()) {
            data.clear()
            data.addAll(newData.filterNotNull().map {
                ItemData(
                    produk = it,
                    isIncrement = false,
                    isChecked = false
                )
            })
            notifyDataSetChanged()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(
        count: Int,
        position: Int,
        isIncrement: Boolean,
        isChecked: Boolean? = null
    ) {
        val unmodifiedItem = data[position]
        val newData = ItemData(
            isIncrement = isIncrement,
            isChecked = isChecked ?: unmodifiedItem.isChecked,
            produk = unmodifiedItem.produk.copy(qty = count)
        )

        if (count <= 0) {
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,data.size)
        } else {
            data[position] = newData
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemKeranjangBinding.inflate(inflater, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        val produk = item.produk
        val binding = holder.binding
        var count = produk.qty

        binding.apply {
            tvJumlah.text = count.toString()
            tvHarga.text = moneyFormatter((produk.harga * count))
            tvNama.text = produk.nama
            Glide.with(imgProduk)
                .load(produk.image.toUri())
                .into(imgProduk)

            btnTambah.setOnClickListener {
                if (item.produk.stok >= count+1) {
                    tvJumlah.text = "${count++}"
                    onItemClickListener.onClick(
                        item.copy(produk = produk.copy(qty = count), isIncrement = true),
                        position
                    )
                    btnTambah.setOnClickListener(null)
                }
            }

            btnKurang.setOnClickListener {
                tvJumlah.text = "${count--}"
                onItemClickListener.onClick(
                    item.copy(produk = produk.copy(qty = count), isIncrement = false),
                    position
                )
                btnKurang.setOnClickListener(null)
            }

            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item.isChecked
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (item.isChecked != isChecked) {
                    onItemCheckBoxListener.onCheckBoxClick(isChecked, produk, position)
                }
            }

            btnDelete.setOnClickListener {
                count = 0
                onItemClickListener.onClick(
                    item.copy(produk = produk.copy(qty = count), isIncrement = false),
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    companion object {
        data class ItemData(
            val produk: ProdukKeranjang,
            val isIncrement: Boolean,
            var isChecked: Boolean = false // should be sealed
        )
    }
}