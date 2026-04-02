package com.example.fooraapp.data.repository

import com.example.fooraapp.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Lấy toàn bộ danh sách (Cho Home Screen)
     */
    fun getAllProducts(onResult: (List<Product>) -> Unit) {
        db.collection("Products").addSnapshotListener { value, error ->
            if (error != null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = mutableListOf<Product>()
            value?.documents?.forEach { doc ->
                val product = doc.toObject(Product::class.java)
                if (product != null) {
                    product.id = doc.id
                    list.add(product)
                }
            }
            onResult(list)
        }
    }

    /**
     * Lấy 1 món ăn cụ thể (Cho Detail Screen)
     */
    fun getProductById(productId: String, onResult: (Product?) -> Unit) {
        db.collection("Products").document(productId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val product = document.toObject(Product::class.java)
                    if (product != null) {
                        product.id = document.id
                    }
                    onResult(product)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}