package com.example.tokoonline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.R
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.databinding.ItemTableBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AdapterPendapatan : RecyclerView.Adapter<AdapterPendapatan.ViewHolder>() {

    private val list: MutableList<Transaction> = mutableListOf()

    inner class ViewHolder(val binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction, position: Int) {
            if (position % 2 == 0) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white2
                    )
                )
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }

            binding.nama.text = transaction.nama
            binding.status.text = transaction.status
            binding.tanggal.text = formatString(transaction.createdAt)
        }

        private fun formatString(createdAt: String): String {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
            val date = df.parse(createdAt)

            val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return outputDateFormat.format(date)
        }
    }

    fun submitList(newList: List<Transaction>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTableBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}