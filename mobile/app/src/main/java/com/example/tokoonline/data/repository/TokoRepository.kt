package com.example.tokoonline.data.repository

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.data.model.Toko
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TokoRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_TOKO)

    companion object {
        @Volatile
        private var INSTANCE: TokoRepository? = null

        fun getInstance(): TokoRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = TokoRepository()
                INSTANCE = instance
                instance
            }
        }
    }
    fun pushToko(toko: Toko, userUid: String, onComplete: (isSuccess: Boolean) -> Unit){
        val tokoRef = databaseReference.child(userUid).push()
        tokoRef.key?.let {
            val addedProduk = toko.copy(id = it)
            tokoRef.setValue(addedProduk)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

}