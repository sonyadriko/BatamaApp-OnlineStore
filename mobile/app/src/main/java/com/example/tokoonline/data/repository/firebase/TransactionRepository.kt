package com.example.tokoonline.data.repository.firebase

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.data.model.firebase.Transaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class TransactionRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_TRANSACTION)

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(): TransactionRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = TransactionRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    fun loadTransaction(): Flow<List<Transaction?>> {
        return databaseReference.multiValueListenerFlow(Transaction::class.java)
    }

    fun addTransaction(transaction: Transaction, onComplete: (isSuccess: Boolean) -> Unit) {
        val transactionRef = databaseReference.push()
        transactionRef.setValue(transaction)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun updateTransaction(transaction: Transaction, onComplete: (isSuccess: Boolean) -> Unit) {
        databaseReference.child(transaction.id)
            .updateChildren(transaction.toMap())
            .addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

    fun removeTransaction(namaTransaction: String, onComplete: (isSuccess: Boolean) -> Unit) {
        databaseReference.child(namaTransaction).removeValue { error, _ ->
            onComplete(error == null)
        }
    }
}