package com.ikhokha.techcheck.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.ikhokha.techcheck.data.remote.dto.ProductDto
import com.ikhokha.techcheck.domain.model.LoggedInUser
import com.ikhokha.techcheck.data.local.model.ProductEntity
import com.ikhokha.techcheck.domain.util.takeNonNull

fun FirebaseUser.toLoggedInUserMap() = LoggedInUser(displayName ?: "")

fun ProductDto.toProductMap() = takeNonNull(
    key, value?.description, imageUrl, value?.price
) { key, description, image, price ->
    ProductEntity(
        id = key,
        description = description,
        image = image,
        price = price
    )
}
