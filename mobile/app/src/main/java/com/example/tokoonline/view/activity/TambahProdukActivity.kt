package com.example.tokoonline.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.example.tokoonline.view.viewmodel.TambahProdukViewModel
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getFormattedTimeMidtrans
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ActivityTambahProdukBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class TambahProdukActivity : BaseActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
    private val viewModel: TambahProdukViewModel by viewModels()
    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageReference = Firebase.storage.reference.child("images/produk")

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.buttonTambahGambarProduk.setOnClickListener {
            //membuka galeri untuk memilih gambar
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_SELECT_IMAGE)
        }


        initListener()
    }

    private fun initListener() = with(binding) {
        btnSbmitProduk.setOnClickListener {
            showProgressDialog()
            val imageReference = storageReference.child(selectedImageUri!!.lastPathSegment!!)


            //mengupload gambar ke firebase storage
            imageReference.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    uploadProduct(imageReference)
                }.addOnFailureListener {
                    dismissProgressDialog()
                    showToast("Gambar gagal diunggah")
                }
        }
    }

    private fun uploadProduct(imageReference: StorageReference) {
        with(binding) {

            imageReference.downloadUrl.addOnSuccessListener { uri ->
                userRepository.getTokoID(userRepository.uid ?: "") { isSuccess, idToko ->
                    if (isSuccess) {

                        val dataProdukNew = Produk(
                            image = uri.toString(),
                            nama = etNamaProduk.text.toString(),
                            harga = etHargaProduk.text.toString().toLong(),
                            deskripsi = etDeskProduk.text.toString(),
                            keyword = etNamaProduk.text.toString().lowercase(),
                            idSeller = idToko ?: "", // Use the idToko here
                            beratProduk = etBeratProduk.text.toString().toDouble(),
                            stok = this.etStok.text.toString().toInt(),
                            createdAt = getFormattedTimeMidtrans(System.currentTimeMillis())
                        )
                        viewModel.addData(dataProdukNew) { isSuccess ->
                            dismissProgressDialog()
                            if (isSuccess) {
                                showToast("Successfully Saved")
                                finish()
                            } else showToast("Failed")
                        }
                    } else {
                        dismissProgressDialog()
                        showToast("Failed to retrieve idToko")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // Mendapatkan URI gambar yang dipilih dari galeri
            selectedImageUri = data.data
            binding.gambarImageView.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val REQUEST_SELECT_IMAGE = 100
    }

}