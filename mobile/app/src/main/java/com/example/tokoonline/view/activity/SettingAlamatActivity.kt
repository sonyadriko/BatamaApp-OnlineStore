package com.example.tokoonline.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.TambahAlamatBaruActivity
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.firebase.Alamat
import com.example.tokoonline.databinding.ActivitySettingAlamat1Binding
import com.example.tokoonline.view.adapter.AlamatAdapter
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import kotlinx.coroutines.launch


class SettingAlamatActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingAlamat1Binding
    private lateinit var viewModel : AlamatViewModel
    private var uuid = ""

    private var isChanged = false
    private lateinit var alamat: Alamat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAlamat1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AlamatViewModel::class.java]

        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
                getAlamat(uuid)

                binding.btnPilihAlamat.setOnClickListener {
                    if (isChanged) {
                        showProgressDialog()
                        val id = alamat.id.toString()
                        viewModel.setDefaultAlamat(id, uuid) { isSuccessful ->
                            if (isSuccessful) {
                                showToast("Alamat default berhasil di update")
                                dismissProgressDialog()
                                finish()
                            } else {
                                // Failed to set the default address
                                // Handle the error or display a message to the user
                            }
                        }
                    } else {
                        showToast("Alamat tidak berubah")
                    }
                }
            }
        }

        binding.toolbar.binding.leftIcon.setOnClickListener {
            if (isChanged) {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Perubahan belum disimpan")
                builder.setMessage("lanjutkan membuang perubahan?")

                builder.setPositiveButton("Yes") { _, _ ->
                    finish()
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

                // Create and show the dialog
                val dialog = builder.create()
                dialog.show()
            } else finish()
        }

//        binding.btnTambahAlamat.setOnClickListener {
//            goToAlamatForm()
//        }



    }

    private fun getAlamat(userUid: String) {
        showProgressDialog()
        viewModel.getAlamat(userUid) { alamatList ->

            val recyclerView: RecyclerView = binding.rvAlamat
            val emptyView: LinearLayout = binding.emptyView
            val viewOn: RelativeLayout = binding.viewOn

            val adapter = AlamatAdapter(alamatList)

            if (alamatList.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                viewOn.visibility = View.GONE

                binding.toolbarNull.binding.leftIcon.setOnClickListener {
                    finish()
                }

                binding.btnTambahAlamat.setOnClickListener {
//                    goToAlamatForm()
                    val intent = Intent(this, TambahAlamatBaruActivity::class.java)
                    startActivity(intent)
                }
            }else {

                emptyView.visibility = View.GONE
                viewOn.visibility = View.VISIBLE


                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter

                adapter.onCardViewClickListener = { alamat, isChanged ->
                    this.isChanged = isChanged
                    this.alamat = alamat
                }

                adapter.onUbahAlamatClickListener = { alamat ->
                    goToAlamatForm(selectedAlamatId = alamat.id)
                }
            }

            dismissProgressDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
                getAlamat(uuid)
            }
        }
    }
}
