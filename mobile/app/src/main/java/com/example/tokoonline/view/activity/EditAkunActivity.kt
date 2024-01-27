package com.example.tokoonline.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.repository.firebase.UserRepository
import com.example.tokoonline.databinding.ActivityEditAkunBinding


class EditAkunActivity : BaseActivity() {

    private lateinit var binding: ActivityEditAkunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.edtEmail.setText(userRepository.email)

        binding.btnRegister.setOnClickListener {

            val oldPassword = binding.edtSandiLama.text.toString()
            val newPassword = binding.edtSandiBaru1.text.toString()
            val newEmail = binding.edtEmail.text.toString()

            userRepository.email = newEmail

            userRepository.changePassword(oldPassword, newPassword) { isSuccess, errorMessage ->
                if (isSuccess) {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}