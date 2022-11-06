package com.ikhokha.techcheck.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingCartItemEntity(
    @PrimaryKey val id: Int? = null,
    val productId: String,
    val quantity: Int,
    @ColumnInfo(defaultValue = "false")
    val purchased: Boolean = false
)