package com.ikhokha.techcheck.ui.summary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikhokha.techcheck.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository
):ViewModel() {

    var summaryUiState by mutableStateOf(SummaryUiState())
        private set

    init {
        viewModelScope.launch {
            val orderId: Int = checkNotNull(savedStateHandle["orderId"])
            summaryUiState = summaryUiState.copy(orderSummary = cartRepository.getOrderSummary(orderId))
        }
    }
}