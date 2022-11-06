package com.ikhokha.techcheck.ui

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ikhokha.techcheck.domain.model.OrderSummary
import com.ikhokha.techcheck.ui.cart.CartScreen
import com.ikhokha.techcheck.ui.common.FabScanner
import com.ikhokha.techcheck.ui.home.CartIcon
import com.ikhokha.techcheck.ui.home.HomeScreen
import com.ikhokha.techcheck.ui.home.HomeUiState
import com.ikhokha.techcheck.ui.home.HomeViewModel
import com.ikhokha.techcheck.ui.login.LoginScreen
import com.ikhokha.techcheck.ui.summary.SummaryScreen
import com.livin.ikhokhatechcheck.R


enum class BusyShopScreen(@StringRes val title: Int) {
    Login(title = R.string.login),
    Home(title = R.string.home),
    Cart(title = R.string.my_cart),
    Summary(title = R.string.order_summary)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusyShopAppBar(
    uiState:HomeUiState,
    currentScreen: BusyShopScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = currentScreen.title == BusyShopScreen.Home.title) {
                CartIcon(uiState = uiState) {
                    navigateToCart()
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun BusyShopApp(
    modifier: Modifier = Modifier,
    homeViewModel:HomeViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = if(backStackEntry?.destination?.route ?.startsWith(BusyShopScreen.Summary.name) == true){
        BusyShopScreen.Summary
    }else{
        BusyShopScreen.valueOf(backStackEntry?.destination?.route?: BusyShopScreen.Login.name)
    }

    var fabScannerVisibility by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(BusyShopScreen.Login.title) }
    val uiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            BusyShopAppBar(
                uiState = uiState,
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navigateToCart = {
                    navController.navigate(BusyShopScreen.Cart.name)
                    title = BusyShopScreen.Cart.title
                }
            )
        },
        floatingActionButton = {
            FabScanner(
                fabScannerVisibility
            ) { productId ->
                homeViewModel.addToCart(productId)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BusyShopScreen.Login.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = BusyShopScreen.Login.name) {
                LoginScreen {
                    navController.popBackStack()
                    navController.navigate(BusyShopScreen.Home.name)
                    title = BusyShopScreen.Home.title
                }
            }
            composable(route = BusyShopScreen.Home.name) {
                HomeScreen{ fabVisibility ->
                    fabScannerVisibility = fabVisibility
                }
            }
            composable(route = BusyShopScreen.Cart.name) {
                CartScreen(
                    navigateToSummary = { orderId ->
                        navController.popBackStack()
                        navController.navigate(BusyShopScreen.Summary.name+"/"+orderId)
                        title = BusyShopScreen.Summary.title
                        fabScannerVisibility = false
                    }
                )
            }
            composable(
                route = BusyShopScreen.Summary.name+"/{orderId}",
                arguments = listOf(
                    navArgument("orderId") {
                        type = NavType.IntType
                    }
                )
            ) { navBackStackEntry ->
                val args = navBackStackEntry.arguments
                val context = LocalContext.current
                SummaryScreen(
                    orderId = args?.getInt("orderId")!!,
                    onSendButtonClicked = { orderSummary->
                        shareOrder(context,orderSummary)
                    }
                )
            }
        }
    }
}

private fun shareOrder(context: Context, orderSummary: OrderSummary) {
    val receipt = StringBuilder()
    receipt.append("Order #"+orderSummary.id.toString()+"\nDate:"+orderSummary.date+"\nTotal:"+orderSummary.total+"\n\n")
    orderSummary.products.forEach {
        receipt.append(it.description+"\nQty:"+it.quantity+"\nPrice:"+it.price+"\n\n")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Busy Shop Order")
        putExtra(Intent.EXTRA_TEXT, receipt.toString())
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_order)
        )
    )
}

