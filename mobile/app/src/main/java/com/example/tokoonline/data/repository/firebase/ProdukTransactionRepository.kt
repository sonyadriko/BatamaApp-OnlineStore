package com.example.tokoonline.data.repository.firebase

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProdukTransactionRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_PRODUK_TRANSACTIONS)

    companion object {
        @Volatile
        private var INSTANCE: ProdukTransactionRepository? = null

        fun getInstance(): ProdukTransactionRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = ProdukTransactionRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    fun addProdukTransaction(produkKeranjang: ProdukKeranjang, onComplete: (isSuccess: Boolean, id: String) -> Unit) {
        val produkRef = databaseReference.push()
        produkRef.setValue(produkKeranjang.copy(id = produkRef.key!!))
            .addOnCompleteListener {
                onComplete(it.isSuccessful, produkRef.key!!)
            }
    }

    fun getProdukById(produkId: String, onComplete: (data: ProdukKeranjang?) -> Unit) {
        databaseReference.child(produkId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val produk = snapshot.getValue(ProdukKeranjang::class.java)
                    onComplete(produk)
                }

                override fun onCancelled(error: DatabaseError) {
                    onComplete(null)
                }
            })
    }
}