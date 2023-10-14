package com.example.tokoonline.view.activity

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.databinding.ActivityAlamatFormBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.google.firebase.storage.StorageReference

class AlamatFormActivity : BaseActivity() {
    private lateinit var binding: ActivityAlamatFormBinding
    private val viewModel : AlamatViewModel by viewModels()

    private fun String.toEditable() : Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlamatFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedAlamatId = intent.getStringExtra("selectedAlamatId")

        if (selectedAlamatId != null) {
            // Fetch the corresponding address using the repository
            viewModel.getAlamatById(selectedAlamatId) { selectedAlamat ->
                if (selectedAlamat != null) {
                    // Populate the form with the data from selectedAlamat
                    binding.tvLabelAlamat.text = selectedAlamat.label.toEditable()
                    binding.tvAlamatPenerima.text = selectedAlamat.alamat.toEditable()
                    binding.tvCatatanAlamat.text = selectedAlamat.catatan.toEditable()
                    binding.tvNamaPenerima.text = selectedAlamat.nama.toEditable()
                    binding.tvPhonePenerima.text = selectedAlamat.phone.toEditable()
                    binding.btnHapusAlamat.visibility = View.VISIBLE
                    binding.btnHapusAlamat.setOnClickListener {
                        viewModel.deleteAlamatById(selectedAlamatId) { isSuccess ->
                            if (isSuccess) {
                                showToast("Alamat berhasil dihapus")
                                goToHomepage()
                            } else {
                                showToast("Deletion failed")
                            }
                        }
                    }
                } else {
                    // Handle the case where the selected address could not be retrieved
                    showToast("Failed to retrieve selected address")
                }
            }
        }else{
            binding.btnHapusAlamat.visibility = View.GONE
        }
        binding.btnSimpanAlamat.setOnClickListener{
            initListener()
        }

    }
    private fun initListener() = with(binding) {
        showProgressDialog()

        val newDataAlamat = Alamat(
            label = tvLabelAlamat.text.toString(),
            alamat = tvAlamatPenerima.text.toString(),
            catatan = tvCatatanAlamat.text.toString(),
            nama = tvNamaPenerima.text.toString(),
            phone = tvPhonePenerima.text.toString(),
            id_users = userRepository.uid
        )

        // Check if the address already exists based on some criteria (e.g., ID)
        val existingAlamatId = binding.root.tag as String? // Retrieve the ID from the tag

        if (existingAlamatId != null) {
            // Update the existing address
            val updatedAlamat = newDataAlamat.copy(id = existingAlamatId)
            viewModel.updateAlamat(updatedAlamat) { isSuccess ->
                dismissProgressDialog()
                if (isSuccess) {
                    showToast("Alamat berhasil diupdate")
                } else {
                    showToast("Alamat tidak berhasil diupdate")
                }
            }
        } else {
            // Add a new address
            viewModel.addAlamat(newDataAlamat) { isSuccess ->
                dismissProgressDialog()
                if (isSuccess) {
                    showToast("Alamat berhasil ditambahkan")
                } else {
                    showToast("Alamat gagal untuk ditambahkan")
                }
            }
        }
    }

}
