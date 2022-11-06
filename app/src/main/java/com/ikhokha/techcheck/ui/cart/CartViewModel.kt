package com.ikhokha.techcheck.ui.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikhokha.techcheck.domain.model.CartItem
import com.ikhokha.techcheck.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
    ) : ViewModel() {
    var orderId by mutableStateOf(-1)
        private set

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun updateQuantity(productId: String, qty:Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, qty)
        }
    }

    fun createOrder(cartItems: List<CartItem>) {
        viewModelScope.launch {
            orderId =  cartRepository.createOrder(cartItems)
        }
    }

    private val _state = mutableStateOf(CartUiState())
    val cartState: State<CartUiState> = _state

    init{
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { updatedCart->
                _state.value = cartState.value.copy(
                    cartItems = updatedCart.map { item->
                        item.price = (item.quantity* item.price)
                        item
                    },
                    total = updatedCart.sumOf { it.price }
                )
            }
        }
    }
}