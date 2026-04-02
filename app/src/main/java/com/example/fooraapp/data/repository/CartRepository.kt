package com.example.fooraapp.data.repository

import com.example.fooraapp.data.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Hàm thêm vào giỏ hàng cá nhân
    fun addToCart(cartItem: CartItem, onComplete: (Boolean) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return

        // Đường dẫn: Carts -> {UID} -> UserCart -> {ProductID}
        val cartRef = db.collection("Carts")
            .document(currentUserId)
            .collection("UserCart")
            .document(cartItem.productId)

        cartRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Nếu món đã có, tăng số lượng
                val oldQuantity = document.getLong("quantity") ?: 1
                cartRef.update("quantity", oldQuantity + 1)
                    .addOnCompleteListener { onComplete(it.isSuccessful) }
            } else {
                // Nếu chưa có, tạo mới hoàn toàn
                cartRef.set(cartItem.copy(userId = currentUserId))
                    .addOnCompleteListener { onComplete(it.isSuccessful) }
            }
        }
    }
}