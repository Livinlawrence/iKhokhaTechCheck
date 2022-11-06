package com.ikhokha.techcheck.data.repository

import com.ikhokha.techcheck.data.local.CartDao
import com.ikhokha.techcheck.data.local.model.ShoppingCartItemEntity
import com.ikhokha.techcheck.data.remote.BusyShopNetworkDataSource
import com.ikhokha.techcheck.domain.model.Product
import com.ikhokha.techcheck.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val busyShopNetworkDataSource: BusyShopNetworkDataSource,
    private val cartDao: CartDao,
) : HomeRepository {

    override suspend fun getProducts(): List<Product> {
        return busyShopNetworkDataSource.getProducts()
    }

    override fun getCartCount(): Flow<Int> = cartDao.getCartCount()


    override suspend fun addToCart(productId: String) {
        cartDao.getCartItemById(productId)?.let {
            cartDao.updateQuantity(productId, it.quantity + 1)
        } ?: run {
            cartDao.addToCart(
                ShoppingCartItemEntity(
                    productId = productId,
                    quantity = 1
                )
            )
        }
    }
}