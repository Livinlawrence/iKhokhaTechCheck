package com.ikhokha.techcheck.ui.login

import com.ikhokha.techcheck.domain.model.LoggedInUser

data class LoginUiState(
    val loggedInUser: LoggedInUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
