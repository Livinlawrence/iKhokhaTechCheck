package com.ikhokha.techcheck.data.remote.dto

data class ProductDto(
    val key: String? = null,
    val value: ValueDto? = null,
    val imageUrl: String?
)

data class ValueDto(
    val image: String? = null,
    val price: Double? = null,
    val description: String? = null
)