package com.ikhokha.techcheck.ui.cart

import com.ikhokha.techcheck.domain.model.CartItem

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val orderId: Int = -1
)