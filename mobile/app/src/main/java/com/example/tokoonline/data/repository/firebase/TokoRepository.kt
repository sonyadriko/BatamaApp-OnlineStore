package com.example.tokoonline.data.repository.firebase

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.data.model.firebase.Toko
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    fun pushToko(toko: Toko, userUid: String, onComplete: (isSuccess: Boolean) -> Unit) {
        val tokoRef = databaseReference.child(userUid).push()
        tokoRef.key?.let {
            val addedProduk = toko.copy(id = it)
            tokoRef.setValue(addedProduk)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

    fun getTokoData(userUid: String, onComplete: (Toko?) -> Unit) {
        val userTokoReference = databaseReference.child(userUid)
        userTokoReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val toko = snapshot.children.firstOrNull()?.getValue(Toko::class.java)
                onComplete(toko)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }
    fun getTokoById(
        id: String,
        userUid: String,
        onComplete: (Toko?) -> Unit
    ) {
        val userRef = databaseReference.child(userUid)

        userRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val toko = snapshot.children.firstOrNull()?.getValue(Toko::class.java)
                onComplete(toko)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }
}