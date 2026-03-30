package com.example.fooraapp.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val mainImageUrl: String = "",
    val subImages: List<String> = emptyList(),
    val category: String = "",
    val description: String = "",
    val searchKeywords: List<String> = emptyList()
)