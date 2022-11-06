package com.ikhokha.techcheck.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.livin.ikhokhatechcheck.BuildConfig
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var email by remember { mutableStateOf(BuildConfig.INTERNAL_USER_USERNAME) }
                    var password by remember { mutableStateOf((BuildConfig.INTERNAL_USER_PASSWORD)) }

                    Spacer(
                        modifier = Modifier.height(40.dp)
                    )
                    Text(
                        text = "Login with firebase",
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    TextField(label = { Text(text = "Email") },
                        value = email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        onValueChange = { email = it })

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    TextField(label = { Text(text = "Password") },
                        value = password,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { password = it })

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    Box(
                        modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)
                    ) {
                        Button(
                            onClick = {
                                loginViewModel.login(
                                    email = email,
                                    password = password
                                )
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Login")
                        }
                    }

                    loginViewModel.loginUiState.loggedInUser?.let {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Welcome ${it.fullName}"
                            )
                        }
                        LaunchedEffect(Unit) {
                            navigateToHome()
                        }
                    }

                    loginViewModel.loginUiState.error?.let {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                it
                            )
                        }
                    }
                }

                if (loginViewModel.loginUiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    )
}
