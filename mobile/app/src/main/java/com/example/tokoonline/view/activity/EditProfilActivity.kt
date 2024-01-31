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
        val userUid = userRepository.uid.toString()

        binding.btnRegister.setOnClickListener {
            // Get values from the editable fields
            val newName = binding.edtNama.text.toString()
            val newPhone = binding.edtPhone.text.toString()
            val newEmail = binding.edtEmail.text.toString()

            userRepository.updateUser(userUid, newName, newEmail, newPhone) { isSuccess ->
                if (isSuccess) {
                    // Data updated successfully in Firebase
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Finish the activity or navigate back
                } else {
                    // Failed to update data in Firebase
                    Toast.makeText(this, "Failed to save data in Firebase", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}