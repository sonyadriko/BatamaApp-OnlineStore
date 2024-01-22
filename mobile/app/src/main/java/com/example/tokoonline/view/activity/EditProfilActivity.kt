package com.example.tokoonline.view.activity

import android.os.Bundle
import android.widget.Toast
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityEditProfilBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditProfilActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.edtNama.setText(userRepository.nama)
        binding.edtPhone.setText(userRepository.phone)
        binding.edtEmail.setText(userRepository.email)

        binding.btnRegister.setOnClickListener {
            // Get values from the editable fields
            val newName = binding.edtNama.text.toString()
            val newPhone = binding.edtPhone.text.toString()

            // Update the data in your UserRepository (or wherever you store user data)
            userRepository.nama = newName
            userRepository.phone = newPhone

            // Perform any additional save operations if needed

            // Optionally, you can show a success message or navigate back
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity or navigate back
        }

    }
}