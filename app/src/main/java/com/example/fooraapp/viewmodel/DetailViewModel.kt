package com.example.fooraapp.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.fooraapp.data.model.CartItem
import com.example.fooraapp.data.model.Product
import com.example.fooraapp.data.repository.CartRepository
import com.example.fooraapp.data.repository.ProductRepository

class DetailViewModel : ViewModel() {
    private val productRepo = ProductRepository()
    private val cartRepo = CartRepository()

    var product by mutableStateOf<Product?>(null)
    var quantity by mutableIntStateOf(1)

    fun fetchProductById(productId: String) {
        productRepo.getProductById(productId) { product = it }
    }

    fun increaseQuantity() { quantity++ }
    fun decreaseQuantity() { if (quantity > 1) quantity-- }

    fun addToCart(onComplete: (Boolean) -> Unit) {
        val p = product ?: return
        val item = CartItem(p.id, p.name, p.price, p.mainImageUrl, quantity)
        cartRepo.addToCart(item, onComplete)
    }
}