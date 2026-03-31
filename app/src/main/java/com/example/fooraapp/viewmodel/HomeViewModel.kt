package com.example.fooraapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fooraapp.model.Product          // ← sửa dòng này
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        db.collection("Products").addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener

            val list = value?.toObjects(Product::class.java) ?: emptyList()

            productList.clear()
            productList.addAll(list)
        }
    }
}