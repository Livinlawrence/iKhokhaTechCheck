package com.ikhokha.techcheck.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val id: String,
    val description: String,
    val image: String,
    val price: Double,
)

fun ProductEntity.asUiModel() = com.ikhokha.techcheck.domain.model.Product(
    id = id,
    description = description,
    image = image,
    price = price
)
