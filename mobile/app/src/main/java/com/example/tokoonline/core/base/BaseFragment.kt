package com.example.tokoonline.core.base

import androidx.appcompat.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tokoonline.R
import com.example.tokoonline.data.repository.UserRepository
import com.example.tokoonline.view.activity.EditProfilActivity
import com.example.tokoonline.view.activity.LoginActivity
import com.example.tokoonline.view.activity.SettingAlamatActivity
import com.example.tokoonline.view.activity.TokoProfileActivity
import com.example.tokoonline.view.activity.TokoSettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


abstract class BaseFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var userRepository: UserRepository
    private lateinit var progressDialog: ProgressDialog
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(context)
        alertDialog = AlertDialog.Builder(requireActivity())
        userRepository = UserRepository.getInstance(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        if (!checkCurrentUserSession()) {
            showToast("Anda harus login terlebih dahulu")
            logout()
        }
    }

    fun showToast(message: String? = null, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message ?: getString(R.string.something_wrong), length).show()
    }

    fun showProgressDialog() {
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun dismissProgressDialog() = progressDialog.dismiss()

    private fun checkCurrentUserSession(): Boolean {
        val uid = userRepository.uid
        return try {
            auth.currentUser != null
                    && requireNotNull(uid) == requireNotNull(auth.currentUser).uid
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        if (auth.currentUser != null) {
            userRepository.erase()
            auth.signOut()
        }

        startActivity(
            Intent(requireActivity(), LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
    fun goToEditProfil() {
        val intent = Intent(requireContext(), EditProfilActivity::class.java)
        startActivity(intent)
    }
    fun goToSettingAlamat() {
        val intent = Intent(requireContext(), SettingAlamatActivity::class.java)
        startActivity(intent)
    }

    fun goToTokoProfile(){
        val intent = Intent(requireContext(), TokoProfileActivity::class.java)
        startActivity(intent)
    }





}