// File: com.example.fooraapp.model.CartItem.kt
package com.example.fooraapp.data.model // Bỏ chữ .data nếu các file khác không có

data class CartItem(
    val productId: String = "",
    val name: String = "",
    val price: Long = 0,
    val imageUrl: String = "",
    var quantity: Int = 1,
    val userId: String = ""
)