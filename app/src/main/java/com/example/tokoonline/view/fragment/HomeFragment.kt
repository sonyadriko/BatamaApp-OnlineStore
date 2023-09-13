package com.example.tokoonline.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.constanst.Constant.arrProdukTer
import com.example.tokoonline.core.constanst.Constant.arrProdukTerlaris
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.view.adapter.AdapterProduk
import com.example.tokoonline.view.activity.DetailProductActivity
import com.example.tokoonline.databinding.FragmentHomeBinding
import com.example.tokoonline.view.activity.DetailProductActivity.Companion.RESULT_DELETE
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
                loadProduk()
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSliderBanner()
        setUpRecycler()
        initProdukList()
        initListener()
        observe()
    }

    private fun initListener() {
        binding.btnAddproduk.setOnClickListener {
            startActivity(Intent(context, TambahProdukActivity::class.java))

        }
    }

    private fun observe() {
        viewModel.produk.observe(viewLifecycleOwner){
            productAdapter.updateUserList(it)
            dismissProgressDialog()
        }
    }

    private fun loadProduk() {
        viewModel.loadProduk()
        showProgressDialog()
    }

    private fun setUpRecycler() {
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayout.HORIZONTAL)
        val layoutManager = LinearLayoutManager(activity)
        productAdapter = AdapterProduk(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvProduk.apply {
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            adapter = adapter
        }
        binding.rvProduk.layoutManager = layoutManager
    }

    private fun initSliderBanner() {
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        binding.vpSlider.adapter = adapterSlider
    }

    private fun initProdukList() = with(binding) {
        rvProdukterlaris.apply {
            adapter = AdapterProduk(this@HomeFragment).apply {
                submitList(arrProdukTerlaris)
            }
        }

        rvProdukter.apply {
            adapter = AdapterProduk(this@HomeFragment).apply {
                submitList(arrProdukTer)
            }
        }
    }

    override fun onItemClick(data: Produk, position: Int) {
        startActivity(DetailProductActivity.createIntent(requireContext(), data))
    }
}