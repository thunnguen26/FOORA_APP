package com.example.fooraapp.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val mainImageUrl: String = "",          // Ảnh đại diện món ăn
    val subImages: List<String> = emptyList(), // Danh sách ảnh chi tiết (Slide show)
    val category: String = "",              // Danh mục (Pizza, Burger...)
    val description: String = "",           // Mô tả món ăn
    val searchKeywords: List<String> = emptyList() // Các từ khóa để tìm kiếm nhanh
)