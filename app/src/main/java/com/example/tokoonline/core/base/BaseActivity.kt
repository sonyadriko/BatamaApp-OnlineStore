package com.example.tokoonline.core.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoonline.MainActivity
import com.example.tokoonline.R
import com.example.tokoonline.data.repository.UserRepository
import com.example.tokoonline.view.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.net.URLEncoder

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    private lateinit var alertDialog: AlertDialog.Builder
    lateinit var userRepository: UserRepository
    private var isBackClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        alertDialog = AlertDialog.Builder(this)
        userRepository = UserRepository.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        if (!checkCurrentUserSession()) {
            showToast("Anda harus login terlebih dahulu")
            logout()
        }
    }

    private fun checkCurrentUserSession(): Boolean {
        val uid = userRepository.uid
        return auth.currentUser != null
                && uid == auth.currentUser?.uid
    }

    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

    fun showDefaultErrorToast() {
        showToast(getString(R.string.something_wrong))
    }

    fun showProgressDialog() {
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun showDialogLogout() {
        alertDialog
            .setMessage("Apakah anda yakin ingin keluar?")
            .setPositiveButton("Ya") { p0, p1 -> finishAffinity() }
            .setNegativeButton("Tidak") { p0, p1 -> p0.dismiss() }
            .show()
    }

    fun dismissProgressDialog() = progressDialog.dismiss()

    fun goToHomepage() {
        if (checkCurrentUserSession()) {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    fun logout() {
        if (checkCurrentUserSession()) {
            auth.signOut()
            userRepository.erase()
        }

        startActivity(
            Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    fun openWhatsApp(nomor: String, pesan: String? = null) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            var url = "https://api.whatsapp.com/send?phone=$nomor"
            if (pesan != null) {
                val holder = url
                url = holder + "&text=" + URLEncoder.encode(pesan, "UTF-8")
            }

            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)

            startActivity(i)
        } catch (e: Exception) {
            Log.e("ERROR WHATSAPP", e.toString())
            showToast("Tidak ada WhatApp")
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            if (isBackClicked) finish()
            else {
                isBackClicked = true
                showToast("Tekan sekali lagi untuk keluar dari aplikasi")
            }
        } else super.onBackPressed()
    }
}