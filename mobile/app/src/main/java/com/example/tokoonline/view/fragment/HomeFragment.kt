package com.example.tokoonline.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.view.adapter.AdapterProduk
import com.example.tokoonline.view.activity.DetailProductActivity
import com.example.tokoonline.databinding.FragmentHomeBinding
import com.example.tokoonline.view.activity.DetailProductActivity.Companion.RESULT_DELETE
import com.example.tokoonline.view.activity.KeranjangActivity
import com.example.tokoonline.view.activity.SearchActivity
import com.example.tokoonline.view.activity.TambahProdukActivity
import com.example.tokoonline.view.adapter.AdapterProdukTerlaris
import com.example.tokoonline.view.viewmodel.ProdukViewModel

class HomeFragment : BaseFragment(), OnItemClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: AdapterProduk
    private lateinit var productTerlarisAdapter : AdapterProdukTerlaris

    private val viewModel: ProdukViewModel by viewModels()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_DELETE) {
                // do nothing for now
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observe()
        viewModel.processEvent(ProdukViewModel.LoadDataProduk)
    }

    private fun initView() {
        productTerlarisAdapter = AdapterProdukTerlaris(this)
        binding.rvProdukTerlaris.apply{
            adapter = productTerlarisAdapter
        }

        productAdapter = AdapterProduk(this)
        binding.rvProduk.apply {
            adapter = productAdapter
        }
    }

    private fun initListener() = with(binding) {
        searchClickable.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        btnAddproduk.setOnClickListener {
            startActivity(Intent(context, TambahProdukActivity::class.java))
        }

        icKeranjang.setOnClickListener {
            startActivity(Intent(activity, KeranjangActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.isLoading) showProgressDialog()
            else {
                dismissProgressDialog()
                if (it.dataProduk.isEmpty()) {
                    showToast(
                        "Tidak ada produk yang bisa ditampilkan untuk saat init",
                        Toast.LENGTH_LONG
                    )
                }
            }

            val filteredData = it.dataProduk.filter { produk -> produk.idSeller != userRepository.uid }
            val filteredDataByStok = it.dataProduk
                .filter { produk -> produk.stok > 0 }
                .filter { produk -> produk.idSeller != userRepository.uid }
                .sortedBy { it.stok }

            productAdapter.submitList(filteredData)
            productTerlarisAdapter.submitList(filteredDataByStok)
        }
    }

    override fun onClick(data: Any, position: Int) {
        startActivity(DetailProductActivity.createIntent(requireContext(), data as Produk))
    }
}