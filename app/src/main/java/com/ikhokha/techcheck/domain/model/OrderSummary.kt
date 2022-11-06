package com.ikhokha.techcheck.domain.model

data class OrderSummary(
    val id: Int,
    val date: String,
    val time: String,
    val total: String,
    val products: List<CartItem>
)
