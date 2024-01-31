package com.example.tokoonline.data.repository.firebase

import android.content.Context
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.constanst.Constant.REFERENCE_USER
import com.example.tokoonline.data.model.firebase.User
import com.example.tokoonline.core.util.toRole
import com.example.tokoonline.data.model.firebase.Toko
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository constructor(
    context: Context
) {
    private val database = FirebaseDatabase.getInstance().getReference(REFERENCE_USER)

    companion object {
        const val ROLE = "role"
        const val UID = "uid"
        const val NAMA = "nama"
        const val EMAIL = "email"
        const val PHONE = "noTelepon"
        const val ID_TOKO = "idToko"

        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val sharedPreferences =
        context.getSharedPreferences("${context.packageName}_pref", Context.MODE_PRIVATE)

    var uid: String? = null
        get() = field ?: getStringFromSharedPref(UID)
        set(value) {
            field = value
            saveStringToSharedPref(UID, value)
        }

    var nama: String? = null
        get() = field ?: getStringFromSharedPref(NAMA)
        set(value) {
            field = value
            saveStringToSharedPref(NAMA, value)
        }

    var email: String? = null
        get() = field ?: getStringFromSharedPref(EMAIL)
        set(value) {
            field = value
            saveStringToSharedPref(EMAIL, value)
        }
    var phone: String? = null
        get() = field ?: getStringFromSharedPref(PHONE)
        set(value) {
            field = value
            saveStringToSharedPref(PHONE, value)
        }

    var role: Constant.Role? = null
        get() = field ?: getStringFromSharedPref(ROLE)?.toRole()
        set(value) {
            field = value
            saveStringToSharedPref(ROLE, value?.toString()?.lowercase())
        }


    fun setUserData(uid: String, user: User) {
        this.role = user.role.toRole()
        this.uid = uid
        this.nama = user.nama
        this.email = user.email
        this.phone = user.noTelepon
    }

    fun erase() {
        uid = null
        role = null
        nama = null
        email = null
        phone = null
    }

    fun getRemoteUserData(userUid: String, onComplete: (isSuccess: Boolean, user: User?) -> Unit) {
        database.child(userUid).get()
            .addOnCompleteListener {
                onComplete(it.isSuccessful, it.result.getValue(User::class.java))
            }
    }

    private fun getStringFromSharedPref(key: String): String? {
        return if (sharedPreferences.contains(key))
            sharedPreferences.getString(key, "")
        else
            null
    }

    private fun saveStringToSharedPref(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        if (value == null)
            editor.remove(key)
        else
            editor.putString(key, value)
        editor.apply()
    }

    fun getTokoID(idUser: String, onComplete: (isSuccess: Boolean, idToko: String?) -> Unit) {
        val tokoReference = FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_TOKO)

        tokoReference.child(idUser).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    val idToko = snapshot.children.firstOrNull()?.getValue(Toko::class.java)?.id
                    onComplete(true, idToko)

                    // Save idToko to SharedPreferences
                    saveStringToSharedPref(ID_TOKO, idToko)
                } else {
                    onComplete(false, null)
                }
            }
    }
    fun getIdToko(): String? {
        return getStringFromSharedPref(ID_TOKO)
    }

    fun updateUser(userUid: String, newName: String, newEmail: String, newPhone: String, onComplete: (isSuccess: Boolean) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: userUid

        val userUpdateMap = mapOf(
            NAMA to newName,
            EMAIL to newEmail,
            PHONE to newPhone
        )

        database.child(uid).updateChildren(userUpdateMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update successful
                    onComplete(true)
                } else {
                    // Update failed
                    onComplete(false)
                }
            }
    }

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onComplete: (isSuccess: Boolean, errorMessage: String?) -> Unit){


        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {

            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Password updated successfully
                                    onComplete(true, null)
                                } else {
                                    // Handle the failure to update the password
                                    onComplete(false, "Failed to update password")
                                }
                            }
                    } else {
                        // Handle the failure to reauthenticate
                        onComplete(false, "Password Lama yang anda masukkan salah")
                    }
                }
        }
    }
}
