package com.ikhokha.techcheck.ui.home

import com.ikhokha.techcheck.domain.model.Product

sealed interface HomeUiState {
    data class Success(val products: List<Product>, val cartCount: Int) : HomeUiState
    data class Error(val message: String? = null) : HomeUiState
    object Loading : HomeUiState
}