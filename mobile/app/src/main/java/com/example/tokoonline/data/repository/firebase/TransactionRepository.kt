package com.example.tokoonline.data.repository.firebase

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.firebase.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        try {
            transactionRef.setValue(transaction.copy(id = transactionRef.key!!))
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        } catch (e: Exception) {
            onComplete(false)
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

    fun getTransactionsByUserId(userId: String, onComplete: (data: List<Transaction?>) -> Unit) {
        databaseReference.orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val transactions = dataSnapshot.children.map { snapshot ->
                        snapshot.getValue(Transaction::class.java)
                    }.reversed()
                    onComplete(transactions)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error here if needed
                    onComplete(emptyList()) // Return an empty list in case of an error
                }
            })
    }

    fun getTransactionsByIdSeller(idSeller: String, onComplete: (data: List<Transaction?>) -> Unit) {
        databaseReference.orderByChild("idSeller")
            .equalTo(idSeller)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val transactions = dataSnapshot.children.map { snapshot ->
                        snapshot.getValue(Transaction::class.java)
                    }.reversed()
                    onComplete(transactions)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error here if needed
                    onComplete(emptyList()) // Return an empty list in case of an error
                }
            })
    }

}