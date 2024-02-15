package com.example.tokoonline.view.activity.toko.pendapatan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.databinding.ActivityPendapatan2Binding
import com.example.tokoonline.view.adapter.AdapterPendapatan

class PendapatanActivity : BaseActivity() {

    private val transactionRepository = TransactionRepository.getInstance()

    private lateinit var binding: ActivityPendapatan2Binding
    private val adapter: AdapterPendapatan by lazy {
        AdapterPendapatan()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendapatan2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        initView()
    }

    private fun initView() = with(binding) {
        rvPendapatan.adapter = adapter
        showProgressDialog()
        transactionRepository.getTransactionsByIdSeller(userRepository.uid!!) {
            val filteredList = it.filterNotNull()
            val pendapatan = filteredList.filter { trx ->
                trx.status == "success"
            }.sumOf { trx ->
                trx.harga
            }

            adapter.submitList(filteredList)
            binding.penghasilan.text = moneyFormatter(pendapatan.toLong())
            dismissProgressDialog()
        }
    }
}