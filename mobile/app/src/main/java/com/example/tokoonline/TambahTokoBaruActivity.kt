package com.example.tokoonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.firebase.Alamat
import com.example.tokoonline.data.model.firebase.Toko
import com.example.tokoonline.data.repository.firebase.AlamatRepository
import com.example.tokoonline.databinding.ActivityTambahTokoBaruBinding
import com.example.tokoonline.databinding.ActivityTokoProfileBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.UUID

class TambahTokoBaruActivity : BaseActivity() {
    private lateinit var binding: ActivityTambahTokoBaruBinding
    private lateinit var viewModelAlamat: AlamatViewModel
    private val alamatRepository: AlamatRepository = AlamatRepository.getInstance()
    private var alamatDefault: Alamat? = null
    private var alamatDataLoaded = false // Flag to track whether alamatDefault has been loaded

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahTokoBaruBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModelAlamat = ViewModelProvider(this)[AlamatViewModel::class.java]
        val userId = userRepository.uid.toString()
        showAlamatDefault(userId)


        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.btnTambahAlamat.setOnClickListener {
            goToAlamatSetting()
            onPause()
        }



        binding.btnSimpanToko.setOnClickListener {
            saveTokoDataToFirebase()
        }

        initListener()
    }

    private fun initListener() {
        
    }

    private fun saveTokoDataToFirebase() {
        val userId = userRepository.uid.toString()

        // Check if alamatDefault is not null
        if (alamatDefault != null) {
            val idAlamat = alamatDefault!!.id
            val namaToko = binding.edtNama.text.toString()

            if (idAlamat!!.isNotEmpty() && namaToko.isNotEmpty()) {
                // Create a Toko object with the obtained data
//                val toko = Toko(
//                    nama = namaToko,
//                    id_alamat = idAlamat,
//                    id_users = userId
//                    // Add other fields as needed
//                )
                val tokoReference = FirebaseDatabase.getInstance().getReference("Toko").child(userId)// Replace "toko" with your table name

                val idToko = tokoReference.push().key

                val tokoReference2 =
                    idToko?.let {
                        FirebaseDatabase.getInstance().getReference("Toko").child(userId).child(
                            it
                        )
                    } // Replace "toko" with your table name
//                val idToko = FirebaseDatabase.getInstance().getReference("users/$userId").push().key

//                if (idToko != null) {
                    // Create a Toko object with the obtained data
                    val tokoData = mapOf(
                        "id" to idToko,
                        "id_alamat" to idAlamat,
                        "id_users" to userId,
                        "nama" to namaToko,
                        // Add other fields as needed
                    )

                    // Save the Toko object to Firebase
//                    val tokoReference = FirebaseDatabase.getInstance()
//                        .getReference("Toko") // Replace "toko" with your table name
//                    val tokoId = tokoReference.push().key // Generate a unique ID for the Toko

                if (tokoReference2 != null) {
                    tokoReference2.setValue(tokoData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Data saved successfully
                                showToast("Toko saved successfully")
                                finish()
                            } else {
                                // Handle the error and log details
                                showToast("Failed to save Toko: ${task.exception?.message}")
                                Log.e("FirebaseError", "Failed to save Toko", task.exception)
                            }
                        }
                }
//                }
            } else {
                showToast("Nama Toko or ID Alamat cannot be empty")
            }
        } else {
            showToast("AlamatDefault is null")
        }
    }


    fun showAlamatDefault(userUid: String) {
        viewModelAlamat.getAlamatDefault(userUid) { alamatDefault ->
            this.alamatDefault = alamatDefault
            alamatDataLoaded = true // Set the flag to true when data is loaded
            if (alamatDefault != null) {
                binding.tvNamaPenerima.text = alamatDefault.nama
                binding.tvAlamatPenerima.text = alamatDefault.alamat
                binding.tvPhonePenerima.text = alamatDefault.phone
                binding.tvTypeAlamat.text = alamatDefault.label
            } else {
                binding.linearlayout.visibility = View.GONE
            }
        }
    }
}