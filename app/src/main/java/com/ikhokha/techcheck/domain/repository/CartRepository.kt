package com.ikhokha.techcheck.domain.repository

import com.ikhokha.techcheck.domain.model.CartItem
import com.ikhokha.techcheck.domain.model.OrderSummary
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems():Flow<List<CartItem>>
    suspend fun removeFromCart(productId: String)
    suspend fun updateQuantity(productId: String, qty:Int)
    suspend fun createOrder(cartItems: List<CartItem>):Int
    suspend fun getOrderSummary(orderId:Int):OrderSummary
}