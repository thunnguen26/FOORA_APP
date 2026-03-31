package com.example.fooraapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fooraapp.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Sử dụng mutableStateListOf để UI tự cập nhật khi dữ liệu Firestore thay đổi
    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        // Lắng nghe sự thay đổi realtime từ collection "Products"
        db.collection("Products").addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener

            // Chuyển đổi dữ liệu từ Firestore sang danh sách Object Product
            val list = value?.toObjects(Product::class.java) ?: emptyList()

            productList.clear()
            productList.addAll(list)
        }
    }
}