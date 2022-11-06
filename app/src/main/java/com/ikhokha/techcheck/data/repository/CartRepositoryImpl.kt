package com.ikhokha.techcheck.data.repository

import android.icu.text.SimpleDateFormat
import com.ikhokha.techcheck.data.local.CartDao
import com.ikhokha.techcheck.data.local.OrderDao
import com.ikhokha.techcheck.data.local.ProductDao
import com.ikhokha.techcheck.data.local.model.OrderEntity
import com.ikhokha.techcheck.domain.model.CartItem
import com.ikhokha.techcheck.domain.model.OrderSummary
import com.ikhokha.techcheck.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
    ) : CartRepository {
    override fun getCartItems(): Flow<List<CartItem>> {
       return cartDao.getCartItems()
    }

    override suspend fun removeFromCart(productId: String) {
       cartDao.removeFromCart(productId)
    }

    override suspend fun updateQuantity(productId: String, qty:Int) {
        cartDao.updateQuantity(productId, qty)
    }

    override suspend fun createOrder(cartItems: List<CartItem>):Int {
        cartDao.updateCartStatus(cartItems.map { it.id })
        val orderDate = System.currentTimeMillis()
        val orderId = Random().nextInt(3000)
        cartItems.forEach {
            orderDao.insertOrder(OrderEntity(
                orderId = orderId,
                productId = it.productId,
                qty = it.quantity,
                date = orderDate
            ))
        }
        return orderId
    }

    override suspend fun getOrderSummary(orderId:Int): OrderSummary {
        val products = mutableListOf<CartItem>()
        val orders =  orderDao.getOrders(orderId)
        val dateTime = orders.first().date
        orders.forEach {
          productDao.getProductById(it.productId)?.let { product->
                products.add(CartItem(
                    id= it.orderId,
                    productId = product.id,
                    image = product.image,
                    description = product.description,
                    price = product.price,
                    quantity = it.qty
                ))
          }
        }
        return OrderSummary(
            id = orderId,
            date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(dateTime)),
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(dateTime)),
            total = "R ${products.sumOf { it.price }}",
            products = products
        )
    }
}