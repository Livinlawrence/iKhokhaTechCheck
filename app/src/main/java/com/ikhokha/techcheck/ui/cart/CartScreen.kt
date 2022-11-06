package com.ikhokha.techcheck.ui.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ikhokha.techcheck.domain.model.CartItem

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(), navigateToSummary: (Int) -> Unit
) {
    val uiState = cartViewModel.cartState.value

    if (cartViewModel.orderId != -1) {
        LaunchedEffect(Unit) {
            navigateToSummary(cartViewModel.orderId)
        }
    }

    CartListScreen(
        uiState = uiState,
        removeFromCart = { cartItem ->
        cartViewModel.removeFromCart(cartItem.productId)
    }, updateQuantity = { id, qty ->
        cartViewModel.updateQuantity(id, qty)
    }, createOrder = {
        cartViewModel.createOrder(uiState.cartItems)
    })
}

@Composable
fun CartListScreen(
    uiState: CartUiState,
    removeFromCart: (CartItem) -> Unit,
    updateQuantity: (String, Int) -> Unit,
    createOrder: () -> Unit
) {
    if (uiState.cartItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Your cart is empty", modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        CartList(
            uiState = uiState,
            removeFromCart = removeFromCart,
            updateQuantity = updateQuantity,
            createOrder = createOrder
        )
    }
}

@Composable
fun CartList(
    uiState: CartUiState,
    removeFromCart: (CartItem) -> Unit,
    updateQuantity: (String, Int) -> Unit,
    createOrder: () -> Unit
) {
    val cartListItems = uiState.cartItems
    Column(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            LazyColumn(contentPadding = PaddingValues(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 30.dp
            )) {
                items(cartListItems) { item ->
                    ShoppingCartItem(
                        cartItem = item,
                        removeFromCart = removeFromCart,
                        updateQuantity = updateQuantity
                    )
                    Divider(
                        modifier = Modifier
                            .height(15.dp),
                        thickness = 0.dp,
                        color = Color.Transparent
                    )
                }
            }
            if (cartListItems.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(color = Color.LightGray)
                ) {
                    Text(
                        text = "Total: ${uiState.total}",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ), modifier = Modifier.padding(10.dp)
                    )
                    OutlinedButton(onClick = {
                        createOrder()
                    }, content = {
                        Text(
                            text = "Checkout",
                            style = TextStyle(
                                color = Color.Blue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    })
                }
            }
        }
    }
}


@Composable
fun ShoppingCartItem(
    cartItem: CartItem,
    removeFromCart: (CartItem) -> Unit,
    updateQuantity: (String, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                ) {
                    AsyncImage(
                        model = cartItem.image,
                        modifier = Modifier.height(130.dp),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = cartItem.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(color = Color.Black, fontSize = 17.sp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "R${cartItem.price}",
                        style = TextStyle(color = Color.Black, fontSize = 15.sp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Qty:", style = TextStyle(color = Color.Black, fontSize = 15.sp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        OutlinedButton(
                            onClick = {
                                var qty = cartItem.quantity
                                if (qty > 1) {
                                    qty -= 1
                                }
                                updateQuantity(
                                    cartItem.productId, qty
                                )
                            },
                            modifier = Modifier.size(30.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.Blue),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Remove")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${cartItem.quantity}",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(color = Color.Black, fontSize = 20.sp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedButton(
                            onClick = {
                                updateQuantity(
                                    cartItem.productId, cartItem.quantity + 1
                                )
                            },
                            modifier = Modifier.size(30.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.Blue),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }

            }
            IconButton(modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
                onClick = { removeFromCart(cartItem) },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "Remove", style = TextStyle(
                                color = Color.Red, fontSize = 17.sp, fontWeight = FontWeight.Bold
                            )
                        )
                    }
                })
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    ShoppingCartItem(cartItem = CartItem(
        id = 1, productId = "", description = "ds", image = "", price = 20.0, quantity = 1
    ), removeFromCart = {

    }, updateQuantity = { _, _ ->

    })
}

