package com.ikhokha.techcheck.domain.repository

import com.ikhokha.techcheck.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getProducts(): List<Product>

    fun getCartCount(): Flow<Int>

    suspend fun addToCart(productId: String)
}