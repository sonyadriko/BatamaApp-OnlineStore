package com.example.tokoonline.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.TambahAlamatBaruActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Alamat
import com.example.tokoonline.databinding.ItemAlamatBinding

class AlamatAdapter(
    private val alamatList: List<Alamat>
) : RecyclerView.Adapter<AlamatAdapter.ViewHolder>() {
    var onUbahAlamatClickListener: ((Alamat) -> Unit)? = null
    var onCardViewClickListener: ((Alamat) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemAlamatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alamat: Alamat, position: Int) = with(binding) {
            if (position == alamatList.lastIndex) {
                btnTambahAlamat2.visible()
            } else {
                btnTambahAlamat2.gone()
            }

            tvNamaPenerima.text = alamat.nama
            tvAlamatPenerima.text = alamat.alamat
            tvPhonePenerima.text = alamat.phone
            tvTypeAlamat.text = alamat.label
            radioButton.isChecked = alamat.default
            rootCard.setOnClickListener {
                showConfirmationDialog(alamat)
            }
            tvUbahAlamat.setOnClickListener {
                onUbahAlamatClickListener?.invoke(alamat)
            }
            btnTambahAlamat2.setOnClickListener {
                val intent = Intent(btnTambahAlamat2.context, TambahAlamatBaruActivity::class.java)
                btnTambahAlamat2.context.startActivity(intent)
            }
        }

        private fun showConfirmationDialog(alamat: Alamat) {
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Confirmation")
            builder.setMessage("Atur alamat ini menjadi Alamat Default?")

            builder.setPositiveButton("Yes") { _, _ ->
                onCardViewClickListener?.invoke(alamat)
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            // Create and show the dialog
            val dialog = builder.create()
            dialog.show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlamatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alamat = alamatList[position]
        holder.bind(alamat, position)
    }

    override fun getItemCount(): Int {
        return alamatList.size
    }
}
