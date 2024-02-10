package com.example.tokoonline.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.ProdukTransactionRepository
import com.example.tokoonline.databinding.ItemRiwayatBinding


class AdapterRiwayat(
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<AdapterRiwayat.ViewHolder>() {

    private val transactionList: MutableList<Transaction> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Transaction>) {
        transactionList.clear()
        transactionList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val produkRepository = ProdukTransactionRepository.getInstance()
        private var adapter: AdapterRiwayatProduk? = null

        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction, position: Int) = with(binding) {
            labelStatus.setStatus(status = transaction.status)
            produkRepository.getProdukById(transaction.produkId) {
                if (adapter == null) {
                    adapter = AdapterRiwayatProduk(
                        context = binding.root.context,
                        it.filterNotNull()
                    )
                }

                binding.rv.adapter = adapter
            }

            binding.root.setOnClickListener {
                onItemClick.onClick(transaction, position)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]
        transaction.let {
            holder.bind(transaction, position)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

}