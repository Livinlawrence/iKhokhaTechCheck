package com.ikhokha.techcheck.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikhokha.techcheck.domain.repository.LoginRepository
import com.ikhokha.techcheck.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUiState = loginUiState.copy(
                isLoading = true,
                error = null
            )
            when (val result = loginRepository.login(
                username = email,
                password = password
            )) {
                is Resource.Success -> {
                    loginUiState = loginUiState.copy(
                        loggedInUser = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    loginUiState = loginUiState.copy(
                        loggedInUser = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}