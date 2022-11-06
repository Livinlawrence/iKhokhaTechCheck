package com.ikhokha.techcheck.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikhokha.techcheck.domain.model.CartItem
import com.ikhokha.techcheck.data.local.model.ShoppingCartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT p.description,p.image,p.price,s.id,s.productId,s.quantity FROM productentity p JOIN shoppingcartitementity  s ON s.productId = p.id WHERE s.purchased=0")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(shoppingCartItemEntity: ShoppingCartItemEntity)

    @Query("SELECT * FROM shoppingcartitementity WHERE productId = :productId AND purchased = 0")
    suspend fun getCartItemById(productId:String): ShoppingCartItemEntity?

    @Query("DELETE FROM shoppingcartitementity WHERE productId = :productId")
    suspend fun removeFromCart(productId: String)

    @Query("UPDATE shoppingcartitementity SET quantity = :qty WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, qty: Int)

    @Query("UPDATE shoppingcartitementity SET purchased = 'true' WHERE id in (:cartIds) ")
    suspend fun updateCartStatus(cartIds: List<Int>)

    @Query("SELECT COUNT(id) FROM shoppingcartitementity WHERE purchased = 0")
    fun getCartCount(): Flow<Int>
}