package com.example.tokoonline.core.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoonline.view.activity.MainActivity
import com.example.tokoonline.R
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.repository.firebase.UserRepository
import com.example.tokoonline.view.activity.AlamatFormActivity
import com.example.tokoonline.view.activity.EditProfilFormActivity
import com.example.tokoonline.view.activity.InitialActivity
import com.example.tokoonline.view.activity.PengirimanActivity
import com.example.tokoonline.view.activity.ProdukSayaActivity
import com.example.tokoonline.view.activity.SettingAlamatActivity
import com.example.tokoonline.view.activity.TokoProfileActivity
import com.example.tokoonline.view.activity.TokoSettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.net.URLEncoder

@Suppress("Deprecation")
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
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
            finish()
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

    fun goToEditProfilForm(cardToShow: String) {
        val intent = Intent(this, EditProfilFormActivity::class.java)
        intent.putExtra("cardToShow", cardToShow)
        startActivity(intent)
    }

    fun goToAlamatForm(selectedAlamatId: String? = null) {
        val intent = Intent(this, AlamatFormActivity::class.java)
        if (selectedAlamatId != null) {
            intent.putExtra("selectedAlamatId", selectedAlamatId)
        }
        startActivity(intent)
    }
    fun goToAlamatSetting(){
        val intent = Intent(this, SettingAlamatActivity::class.java)
        startActivity(intent)
    }

    fun goToBayar(totalBelanja: Long, produkList: List<ProdukKeranjang>){
        val intent = Intent(this, PengirimanActivity::class.java )
        val bundle = Bundle()
        if (produkList != null) {
            bundle.putParcelableArrayList("produkList", ArrayList(produkList))
            intent.putExtra("totalBelanja", totalBelanja)
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun goToTokoSetting(){
        val intent = Intent(this, TokoSettingActivity::class.java)
        startActivity(intent)
    }

    fun goToTokoProfile(){
        val intent = Intent(this, TokoProfileActivity::class.java)
        startActivity(intent)
    }


    fun logout() {
        if (checkCurrentUserSession()) {
            auth.signOut()
            userRepository.erase()
        }

        startActivity(
            Intent(this, InitialActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    fun openWhatsApp(nomor: String, pesan: String? = null) {
        try {

            val newNomor = if (nomor[0].equals('0', true)) {
                 nomor.replaceFirst("0", "+62")
            } else nomor
            val i = Intent(Intent.ACTION_VIEW)
            var url = "https://api.whatsapp.com/send?phone=$newNomor"
            if (pesan != null) {
                val holder = url
                url = holder + "&text=" + URLEncoder.encode(pesan, "UTF-8")
            }

            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)

            startActivity(i)
        } catch (e: Exception) {
            Timber.tag("ERROR WHATSAPP").e(e.toString())
            showToast("Tidak ada WhatApp")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isTaskRoot) {
            if (isBackClicked) finish()
            else {
                isBackClicked = true
                showToast("Tekan sekali lagi untuk keluar dari aplikasi")
            }
        } else super.onBackPressed()
    }

    fun goToProdukSaya(tokoID: String? = null) {
        val intent = Intent(this, ProdukSayaActivity::class.java)
        if (tokoID != null) {
            intent.putExtra("tokoID", tokoID)
        }
        startActivity(intent)
    }
}