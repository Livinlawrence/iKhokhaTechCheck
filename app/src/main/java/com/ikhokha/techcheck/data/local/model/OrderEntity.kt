package com.ikhokha.techcheck.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderEntity(
    @PrimaryKey val id: Int? = null,
    val orderId: Int,
    val productId: String,
    val qty: Int,
    val date: Long
)