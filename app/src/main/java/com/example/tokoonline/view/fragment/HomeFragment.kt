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
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.view.adapter.AdapterProduk
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.databinding.FragmentHomeBinding
import com.example.tokoonline.view.activity.DetailProdukActivity
import com.example.tokoonline.view.activity.DetailProdukActivity.Companion.RESULT_DELETE
import com.example.tokoonline.view.activity.TambahProdukActivity
import com.example.tokoonline.view.adapter.AdapterSlider
import com.example.tokoonline.view.viewmodel.ProdukViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class HomeFragment : BaseFragment(), OnItemClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var produkRecyclerView: RecyclerView
    private lateinit var adapter: AdapterProduk

//    private val viewModel by viewModels<ProdukViewModel>()
    private val viewModel: ProdukViewModel by viewModels()

    var databaseReference = FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_PRODUK)
    val arrProduk: MutableList<Produk> = mutableListOf()


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
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setUpRecycler()
//        loadData()
        observe()
        loadProduk()
    }

    private fun observe() {
        viewModel.produk.observe(viewLifecycleOwner){
            adapter.updateUserList(it)
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
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        produkRecyclerView = binding.rvProduk
        produkRecyclerView.addItemDecoration(dividerItemDecoration)
        produkRecyclerView.setHasFixedSize(true)
        adapter = AdapterProduk(this)
        produkRecyclerView.adapter = adapter
        binding.rvProduk.layoutManager = layoutManager
    }

    private fun initListener() {
        binding.btnAddproduk.setOnClickListener {
            startActivity(Intent(context, TambahProdukActivity::class.java))

        }

        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        binding.vpSlider.adapter = adapterSlider


        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL



        binding.rvProdukterlaris.adapter = AdapterProduk(this).apply {
            submitList(arrProdukTerlaris)
        }
        binding.rvProdukterlaris.layoutManager = layoutManager2

        binding.rvProdukter.adapter = AdapterProduk(this).apply {
            submitList(arrProdukTer)
        }
        binding.rvProdukter.layoutManager = layoutManager3


    }


//    private fun loadData() {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                arrProduk.clear()
//                for (postSnapshot in dataSnapshot.children) {
//                    val produk = postSnapshot.getValue(Produk::class.java)
//                    produk?.let {
//                        arrProduk.add(it)
//                    }
//                }
//
//                // Setelah data diambil, update adapter RecyclerView
//                binding.rvProduk.adapter?.notifyDataSetChanged()
//                binding.rvProduk.adapter = AdapterProduk(this).apply {
//                    submitList(arrProduk)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle database error
//            }
//        })
//    }




//    val arrProduk: ArrayList<Produk>get() {
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama = "Hp Iphone dan Android baru"
//        p1.harga = 15980000
////        p1.gambar = R.drawable.slider1
//
//        val p2 = Produk()
//        p2.nama = "Perabotan memasak paling murah, banyak pilihan warna, anti lengket dan bisa menggoreng tanpa minyak"
//        p2.harga = 15980000
////        p2.gambar = R.drawable.slider2
//
//        val p3 = Produk()
//        p3.nama = "Perabotan memasak paling murah, anti lengket dan bisa menggoreng tanpa minyak"
//        p3.harga = 15980000
////        p3.gambar = R.drawable.slider3
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//
//        return arr
//    }
    val arrProdukTerlaris: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP 14_bs749tu"
        p1.harga = 15980000
//        p1.gambar = R.drawable.slider2

        val p2 = Produk()
        p2.nama = "Hp Envy_13_aq0019tx"
        p2.harga = 15980000
//        p2.gambar = R.drawable.slider3

        val p3 = Produk()
        p3.nama = "HP pavilion_13_an0006na"
        p3.harga = 15980000
//        p3.gambar = R.drawable.slider1

        val p4 = Produk()
        p4.nama = "Hp Envy_13_aq0019tx"
        p4.harga = 15980000
//        p4.gambar = R.drawable.slider3

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        arr.add(p4)

        return arr
    }

    val arrProdukTer: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP 14_bs749tu"
        p1.harga = 15980000
//        p1.gambar = R.drawable.slider3

        val p2 = Produk()
        p2.nama = "Hp Envy_13_aq0019tx"
        p2.harga = 15980000
//        p2.gambar = R.drawable.slider1

        val p3 = Produk()
        p3.nama = "HP pavilion_13_an0006na"
        p3.harga = 15980000
//        p3.gambar = R.drawable.slider2

        val p4 = Produk()
        p4.nama = "Hp Envy_13_aq0019tx"
        p4.harga = 15980000
//        p4.gambar = R.drawable.slider1

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        arr.add(p4)

       return arr
    }

    override fun onClick(data: Any, position: Int) {
        val currentItem = data as Produk
        val detailProduk = Intent(context, DetailProdukActivity::class.java).apply {
            putExtra("namaproduk", currentItem.nama)
        }
        startForResult.launch(detailProduk)
    }


}



