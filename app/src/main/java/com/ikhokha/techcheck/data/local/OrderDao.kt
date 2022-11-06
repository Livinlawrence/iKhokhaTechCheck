package com.ikhokha.techcheck.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ikhokha.techcheck.data.local.model.OrderEntity

@Dao
interface OrderDao {

    @Query("SELECT *FROM orderentity WHERE orderId =:id")
    suspend fun getOrders(id:Int): List<OrderEntity>

    @Insert
    suspend fun insertOrder(orderEntity: OrderEntity)
}