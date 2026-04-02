package com.example.fooraapp.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Product(
    var id          : String       = "",
    val name        : String       = "",
    val description : String       = "",
    val price       : Long         = 0,
    val category    : String       = "",

    // Firestore dùng "mainImageUrl" — map đúng tên field
    @get:PropertyName("mainImageUrl")
    @set:PropertyName("mainImageUrl")
    var mainImageUrl: String       = "",

    val searchKeywords: List<String> = emptyList(),
    val subImages     : List<String> = emptyList(),

    // Các field UI — không lưu trên Firestore, dùng default
    val rating      : Float  = 4.8f,
    val deliveryTime: String = "20-30 phút",
    val isPopular   : Boolean = false
)