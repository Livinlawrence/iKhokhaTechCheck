package com.ikhokha.techcheck.domain.model

data class CartItem(
    val id: Int,
    val productId: String,
    val description: String,
    val image: String,
    var price: Double,
    val quantity:Int)
