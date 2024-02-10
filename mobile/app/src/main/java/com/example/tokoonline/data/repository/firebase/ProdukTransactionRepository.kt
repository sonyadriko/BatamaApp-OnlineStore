package com.example.tokoonline.data.repository.firebase

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValue
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.collect

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

    fun getRef() = databaseReference.push()

    fun addProdukTransaction(
        parentRef: String?,
        produkKeranjang: ProdukKeranjang,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        val produkRef = databaseReference.child(parentRef!!).push()
        produkRef.setValue(produkKeranjang.copy(id = produkRef.key!!))
            .addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

    fun getProdukById(produkId: String, onComplete: (data: List<ProdukKeranjang?>) -> Unit) {
        databaseReference.child(produkId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                onComplete(dataSnapshot.children.map { snapshot ->
                    snapshot.getValue(ProdukKeranjang::class.java)
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here if needed
                onComplete(emptyList()) // Return an empty list in case of an error
            }
        })
    }
}