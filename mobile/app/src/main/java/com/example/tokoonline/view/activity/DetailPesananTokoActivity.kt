package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.data.repository.firebase.ProdukTransactionRepository
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.databinding.ActivityDetailPesananTokoBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class DetailPesananTokoActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailPesananTokoBinding
    private lateinit var viewModelAlamat: AlamatViewModel
    private lateinit var viewModelToko: TokoViewModel

    private val transactionRepository = TransactionRepository.getInstance()

    companion object {
        const val STATUS_PENDING = "pending"
        const val STATUS_CANCELED = "canceled"
        const val STATUS_SUCCESS = "success"

        private const val EXTRA_DATA = "data"
        private const val EXTRA_IS_SELLER = "is_seller"
        fun createIntent(
            context: Context,
            transaction: Transaction?,
            isFromSeller: Boolean = false
        ): Intent {
            return Intent(context, DetailPesananTokoActivity::class.java).apply {
                putExtra(EXTRA_DATA, transaction)
                putExtra(EXTRA_IS_SELLER, isFromSeller)
            }
        }
    }

    private val data: Transaction by lazy {
        try {
            intent?.getParcelableExtra(EXTRA_DATA)!!
        } catch (e: Exception) {
            showToast(getString(R.string.something_wrong))
            finish()
            Transaction()
        }
    }
    private val isFromSeller: Boolean by lazy {
        try {
            intent?.getBooleanExtra(EXTRA_IS_SELLER, false)!!
        } catch (e: Exception) {
            showToast(getString(R.string.something_wrong))
            finish()
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)
        viewModelToko = ViewModelProvider(this).get(TokoViewModel::class.java)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        if (isFromSeller) {
            binding.sellerAction.visible()
            binding.btnSelesai.setOnClickListener {
                showProgressDialog()
                transactionRepository
                    .updateTransaction(transaction = data.copy(status = STATUS_SUCCESS)) {
                        dismissProgressDialog()
                        if (it) finish()
                        else showToast("Gagal update status transaksi")
                    }
            }

            binding.btnBatal.setOnClickListener {
                showProgressDialog()
                transactionRepository
                    .updateTransaction(transaction = data.copy(status = STATUS_CANCELED)) {
                        dismissProgressDialog()
                        if (it) finish()
                        else showToast("Gagal update status transaksi")
                    }
            }
        } else binding.sellerAction.gone()

        val userId = userRepository.uid.toString()
        when (data.status?.toLowerCase()) {
            STATUS_PENDING -> {
                binding.statusCancel.gone()
                binding.statusSuccess.gone()
            }

            STATUS_CANCELED -> {
                binding.statusPending.gone()
                binding.statusSuccess.gone()
            }

            STATUS_SUCCESS -> {
                binding.statusPending.gone()
                binding.statusCancel.gone()
            }
        }

        val idAlamat = data.alamatId
        binding.alamatDefault.text = idAlamat

        viewModelAlamat.getAlamatById(idAlamat, userId){alamatData ->
            if (alamatData !== null){
                binding.alamatDefault.visible()
                binding.alamatDefault.text = "${alamatData.nama} \u2022 ${alamatData.phone}\n ${alamatData.alamat}"
            }
        }

        var idProduk = data.produkId
        ProdukTransactionRepository.getInstance().getProdukById(idProduk){produk->
            if (produk !== null){
                binding.tvNama.text = produk.nama
                binding.tvTotalHarga.text = moneyFormatter(produk.harga)
                binding.tvWeight.text = produk.beratProduk.toString()
                Glide.with(this)
                    .load(produk.image)
                    .placeholder(R.drawable.loading_animation) // Placeholder image while loading
                    .into(binding.imgProduk)


                viewModelToko.getTokoData(produk.idSeller.toString()){toko ->
                    if (toko !== null){
                        viewModelAlamat.getAlamatById(toko.id_alamat, toko.id_users){alamatToko ->
                            if (alamatToko !== null){
                                binding.tvAlamatPenjual.text = "${toko.nama} \n ${alamatToko.alamat}"
                            }else{
                                showToast("Gagal mengambil alamat Toko")
                            }
                        }
                    }
                }
            }
        }



        binding.tvMetodePembayaran.visibility = View.VISIBLE


        if (data != null) {
            binding.tvMetodePembayaran.text = "${data.metodePembayaran}"
        }

        binding.tvEstimasi.visible()
        if (data != null) {
            binding.tvEstimasi.text = "${data.metodePengiriman}"
        }
        binding.tvTotalBelanja.visible()
        if (data != null) {
            binding.tvTotalBelanja.text = "${data.harga}"
        }
        binding.tvTotal.visible()
        if (data != null) {
            binding.tvTotal.text = "${data.harga.toDouble()}"
        }

        binding.tvAlamatPenjual.visible()
//        if (data != null){
//            binding.tvAlamatPenjual.text = "${data.produkId}"
//        }


        val produkId = data?.produkId

        if (produkId !== null){

        }
    }

}