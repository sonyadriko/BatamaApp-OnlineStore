package com.example.tokoonline.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.ProdukTransactionRepository
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.databinding.ActivityDetailPesananTokoBinding
import com.example.tokoonline.view.adapter.AdapterItemDetailPesanan
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class DetailPesananTokoActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailPesananTokoBinding
    private lateinit var viewModelAlamat: AlamatViewModel
    private lateinit var viewModelToko: TokoViewModel

    private val transactionRepository = TransactionRepository.getInstance()
    private val produkTransactionRepository = ProdukTransactionRepository.getInstance()

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

    private val adapterDetailPesanan: AdapterItemDetailPesanan by lazy {
        AdapterItemDetailPesanan()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this)[AlamatViewModel::class.java]
        viewModelToko = ViewModelProvider(this)[TokoViewModel::class.java]

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
        val idPembeli = data.userId
        var idProduk = data.produkId

        viewModelAlamat.getAlamatById(idAlamat, idPembeli){alamatData ->
            if (alamatData !== null){
                binding.alamatDefault.text = "${alamatData.nama} \u2022 ${alamatData.phone}\n ${alamatData.alamat}"
            }else{
                viewModelAlamat.getAlamatDefault(idPembeli){alamatDataDefault ->
                    binding.alamatDefault.text = "${alamatDataDefault?.nama} \u2022 ${alamatDataDefault?.phone}\n ${alamatDataDefault?.alamat}"
                }
            }
        }

        ProdukTransactionRepository.getInstance().getProdukById(idProduk){ produk ->
            viewModelToko.getTokoData(produk[0]!!.idSeller.toString()) { toko ->
                val idPembeli = toko?.id_users.toString()


                if (toko !== null) {
                    viewModelAlamat.getAlamatById(toko.id_alamat, toko.id_users) { alamatToko ->
                        if (alamatToko !== null) {
                            binding.tvAlamatPenjual.text = "${toko.nama} \n ${alamatToko.alamat}"
                        } else {
                            viewModelAlamat.getAlamatDefault(toko.id_users){alamatTokoDefault ->
                                binding.tvAlamatPenjual.text = "${toko.nama} \n ${alamatTokoDefault?.alamat}"
                            }
                        }
                    }
                }
            }
        }



        binding.tvMetodePembayaran.visible()
        val metodePembayaran = data.metodePembayaran
        if (metodePembayaran == "cod"){
            binding.tvMetodePembayaran.text = "Cash On Delivery (COD)"
        }else{
            binding.tvMetodePembayaran.text = "Transfer via Gopay"
        }

        binding.tvEstimasi.visible()
        val metodePengiriman = data.metodePengiriman
        if (metodePengiriman == "ambil"){
            binding.tvEstimasi.text = "Diambil oleh Pembeli\n1 Hari Kerja"
        }else{
            binding.tvEstimasi.text = "Dikirim oleh Penjual\n1 Hari Kerja"
        }
        binding.tvTotalBelanja.visible()
        binding.tvTotalBelanja.text = "${data.harga}"
        binding.tvTotal.visible()
        binding.tvTotal.text = "${data.harga}"

        binding.tvAlamatPenjual.visible()

        val produkId = data.produkId
        produkTransactionRepository.getProdukById(produkId) {
            binding.rvBarangPesanan.adapter = adapterDetailPesanan
            adapterDetailPesanan.submitList(it.filterNotNull())
        }
    }

}