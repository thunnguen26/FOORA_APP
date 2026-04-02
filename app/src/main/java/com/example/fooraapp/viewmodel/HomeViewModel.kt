package com.example.fooraapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fooraapp.data.model.Product
import com.example.fooraapp.data.repository.ProductRepository // Import Repository mới

class HomeViewModel : ViewModel() {
    // 1. Khởi tạo Repository thay vì gọi trực tiếp Firestore
    private val repository = ProductRepository()

    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        // 2. Nhờ Repository "đi lấy" dữ liệu hộ
        repository.getAllProducts { list ->
            productList.clear()
            productList.addAll(list)
        }
    }
}