package com.example.tokoonline.core.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.MainActivity
import com.example.tokoonline.R
import com.example.tokoonline.view.activity.LoginActivity
import com.example.tokoonline.view.activity.RegisterActivity
import com.example.tokoonline.core.constanst.Constant.REFERENCE_USER
import com.example.tokoonline.data.domain.User
import com.example.tokoonline.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Suppress("Deprecation")
abstract class BaseAuthActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        userRepository = UserRepository.getInstance(this)
        progressDialog = ProgressDialog(this)
    }

    private fun save() {
        try {
            lifecycleScope.launch {
                if (auth.currentUser?.uid != userRepository.uid) {
                    setDataToLocal(auth.currentUser!!.uid)
                } else forceOut()
            }
        } catch (e: Exception) {
            Log.e("AUTH_EXCEPTION", e.message.toString())
            showToast(getString(R.string.something_wrong))
        }
    }

    private fun setDataToLocal(currentUserUid: String) {
        userRepository.getRemoteUserData(currentUserUid) { isSuccess, user ->
            if (!isSuccess || user == null) {
                forceOut()
                return@getRemoteUserData
            }

            userRepository.setUserData(
                uid = currentUserUid,
                user = user
            )

            dismissProgressDialog()
            goToHomeActivity()
        }
    }

    private fun forceOut() {
        auth.signOut()
        userRepository.erase()
        Toast.makeText(
            this,
            "Data user tidak ditemukan",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun login(email: String, password: String, doOnFailed: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) save()
                else doOnFailed()
            }
    }


    fun register(userDomain: User, callback: (isSuccess: Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(userDomain.email, userDomain.password)
            .addOnCompleteListener {
                val user = auth.currentUser
                val uid = user?.uid
                if (uid != null) {
                    saveUserToDatabase(uid, userDomain) { isSuccess ->
                        callback(isSuccess)
                    }
                } else callback(false)
            }
    }

    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

    fun showProgressDialog() {
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun dismissProgressDialog() = progressDialog.dismiss()

    private fun saveUserToDatabase(
        uid: String,
        userDomain: User,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        database.reference.child(REFERENCE_USER).child(uid).setValue(userDomain.toMap())
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun goToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goToHomeActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}