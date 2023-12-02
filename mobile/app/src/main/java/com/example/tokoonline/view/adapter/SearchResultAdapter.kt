package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.databinding.SearchResultItemBinding

class SearchResultAdapter(private val onItemClickListener: OnItemClick) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultVH>() {

    private val data: MutableList<Produk> = mutableListOf()

    class SearchResultVH(val binding: SearchResultItemBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newData: List<Produk>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchResultItemBinding.inflate(inflater, parent, false)
        return SearchResultVH(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SearchResultVH, position: Int) = with(holder.binding) {
        val currentItem = data[position]

        itemTitle.text = currentItem.nama
        itemPrice.text = currentItem.deskripsi
        Glide.with(cover)
            .load(currentItem.image)
            .placeholder(R.drawable.ic_account_circle_black_24dp)
            .into(cover)

        root.setOnClickListener {
            onItemClickListener.onClick(currentItem, position)
        }
    }

}