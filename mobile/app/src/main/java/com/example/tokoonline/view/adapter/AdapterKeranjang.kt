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
import com.example.tokoonline.view.viewmodel.KeranjangViewModel
import timber.log.Timber

class AdapterKeranjang(
    private val viewModel : KeranjangViewModel,
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<AdapterKeranjang.Holder>() {
    class Holder(val binding: ItemKeranjangBinding) : RecyclerView.ViewHolder(binding.root)

    private val data = mutableListOf<ProdukKeranjang?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newData: List<ProdukKeranjang?>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateData(
        count: Int,
        position: Int,
        isIncrement: Boolean?,
        isChecked: Boolean? = null
    ) {
        val item = data[position] ?: return
        var copied = item.copy(jumlah = count)
        if (isChecked != null) {
            copied = copied.copy(isChecked = isChecked)
        }

        data[position] = copied

        onItemClickListener.onItemClick(
            ItemData(
                copied,
                isIncrement = isIncrement,
            ), position
        )
        notifyItemChanged(position)
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

        binding.apply {
            tvJumlah.text = count.toString()
            tvHarga.text = moneyFormatter((item.harga * count))
            tvNama.text = item.nama
            Glide.with(imgProduk)
                .load(item.image.toUri())
                .into(imgProduk)

            btnTambah.setOnClickListener {
                tvJumlah.text = "${count++}"
                updateData(count, position, true)
            }

            btnKurang.setOnClickListener {
                tvJumlah.text = "${count--}"
                updateData(count, position, false)
            }

            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item.isChecked
            Timber.d("__item__ atas = isChecked: ${item.isChecked}")
            checkBox.setOnCheckedChangeListener { _, b ->
                Timber.d("__item__ = isChecked: ${item.isChecked} && $b")
                if (item.isChecked != b) {
                    item.isChecked = b
                    updateData(count, position, null, b)
                    val price = item.harga * count
                    if (b) {
                        viewModel.addTotalBelanja(price)
                    } else {
                        viewModel.removeTotalBelanja(price)
                    }
                }
            }

            btnDelete.setOnClickListener {
                count = 0;
                updateData(count, position, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    companion object {
        data class ItemData(
            val item: ProdukKeranjang,
            val isIncrement: Boolean?,
        )
    }
}