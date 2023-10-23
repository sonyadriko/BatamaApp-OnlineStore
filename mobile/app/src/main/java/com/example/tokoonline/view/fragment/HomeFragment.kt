package com.example.tokoonline.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.view.adapter.AdapterProduk
import com.example.tokoonline.view.activity.DetailProductActivity
import com.example.tokoonline.databinding.FragmentHomeBinding
import com.example.tokoonline.view.activity.DetailProductActivity.Companion.RESULT_DELETE
import com.example.tokoonline.view.activity.KeranjangActivity
import com.example.tokoonline.view.activity.TambahProdukActivity
import com.example.tokoonline.view.adapter.AdapterSlider
import com.example.tokoonline.view.viewmodel.ProdukViewModel

class HomeFragment : BaseFragment(), OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: AdapterProduk

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
        // slider
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        binding.vpSlider.adapter = adapterSlider


        // produk terbaru
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayout.HORIZONTAL)
        productAdapter = AdapterProduk(this)
        binding.rvProduk.apply {
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            adapter = productAdapter
        }


    }

    private fun initListener() {
        binding.btnAddproduk.setOnClickListener {
            startActivity(Intent(context, TambahProdukActivity::class.java))
        }

        binding.icKeranjang.setOnClickListener {
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


            productAdapter.submitList(it.dataProduk)
        }
    }

    override fun onItemClick(data: Any, position: Int) {
        startActivity(DetailProductActivity.createIntent(requireContext(), data as Produk))
    }
}