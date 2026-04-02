package com.example.fooraapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fooraapp.data.model.CartItem
import com.example.fooraapp.data.model.Product
import com.example.fooraapp.data.repository.CartRepository

class CartViewModel : ViewModel() {
    // Khởi tạo Repository để xử lý logic lưu trữ
    private val repository = CartRepository()

    /**
     * Hàm này được gọi khi người dùng nhấn nút (+) trên ProductCard
     * Nó sẽ chuyển đổi đối tượng Product thành CartItem để lưu vào giỏ
     */
    fun addProductToCart(product: Product) {
        // Tạo một đối tượng CartItem mới (mặc định số lượng là 1)
        val newItem = CartItem(
            productId = product.id,
            name      = product.name,
            price     = product.price,
            imageUrl  = product.mainImageUrl,
            quantity  = 1
            // userId sẽ được Repository tự động lấy từ FirebaseAuth.uid
        )

        // Gọi Repository để đẩy dữ liệu lên Firestore
        repository.addToCart(newItem) { success ->
            if (success) {
                // Thuần có thể thêm logic thông báo tại đây (ví dụ: hiện Toast)
                println("Đã thêm ${product.name} vào giỏ hàng thành công!")
            } else {
                println("Lỗi khi thêm vào giỏ hàng!")
            }
        }
    }
}