package com.example.tokoonline.data.repository.firebase

import android.content.Context
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.ProdukKeranjang
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow


class KeranjangRepository constructor(
    context: Context
) {

    private val database = FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_KERANJANG)
    private val sharedPreferences =
        context.getSharedPreferences("${context.packageName}_pref", Context.MODE_PRIVATE)

    fun getKeranjang(userUid: String): Flow<List<ProdukKeranjang?>> {
        return database.child(userUid).multiValueListenerFlow(ProdukKeranjang::class.java)
    }

    fun addKeranjang(
        userUid: String,
        produk: ProdukKeranjang,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        val produkRef = database.child(userUid).push()
        produkRef.key?.let {
            val addedProduk = produk.copy(id = it)
            produkRef.setValue(addedProduk)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

    fun updateKeranjang(
        userUid: String,
        produk: ProdukKeranjang,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        val produkRef = database.child(userUid).child(produk.id)
        produkRef.updateChildren(produk.toMap())
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun removeKeranjang(
        userUid: String,
        produk: ProdukKeranjang,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        val produkRef = database.child(userUid).child(produk.id)
        produkRef.removeValue()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
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
}