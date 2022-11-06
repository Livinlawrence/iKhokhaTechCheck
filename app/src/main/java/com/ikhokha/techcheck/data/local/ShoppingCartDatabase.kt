package com.ikhokha.techcheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikhokha.techcheck.data.local.model.OrderEntity
import com.ikhokha.techcheck.data.local.model.ProductEntity
import com.ikhokha.techcheck.data.local.model.ShoppingCartItemEntity

@Database(
    entities = [
        ProductEntity::class,
        ShoppingCartItemEntity::class,
        OrderEntity::class
    ],
    version = 1
)
abstract class ShoppingCartDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val cartDao: CartDao
    abstract val orderDao: OrderDao

    companion object {
        const val DATABASE_NAME = "shopping_cart_db"
    }
}