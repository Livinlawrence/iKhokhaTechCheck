package com.ikhokha.techcheck.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikhokha.techcheck.domain.model.Product
import com.ikhokha.techcheck.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {
    private var cartCountState: MutableStateFlow<Int> =
        MutableStateFlow(0)
    private var productsState: MutableStateFlow<List<Product>> =
        MutableStateFlow(listOf())

    init {
        viewModelScope.launch {
            productsState.value = homeRepository.getProducts()

            homeRepository.getCartCount().collectLatest {
                cartCountState.value = it
            }
        }
    }

    val homeUiState = combine(
        productsState,
        cartCountState
    ) { products, cartItems ->
        if (products.isEmpty()) {
            HomeUiState.Loading
        } else {
            HomeUiState.Success(products, cartItems)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState.Loading
    )

    fun addToCart(productId: String) {
        viewModelScope.launch {
            homeRepository.addToCart(productId)
        }
    }
}