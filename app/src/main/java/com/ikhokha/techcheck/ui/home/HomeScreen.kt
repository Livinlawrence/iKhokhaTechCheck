package com.ikhokha.techcheck.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ikhokha.techcheck.domain.model.Product

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    fabScannerVisibility: (Boolean) ->Unit
) {
    val uiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    ProductListScreen(
        uiState = uiState,
        fabVisibility = { b ->
            fabScannerVisibility(b)
        }) { productId ->
        homeViewModel.addToCart(productId)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CartIcon(
    uiState: HomeUiState, navigateToCart: () -> Unit
) {
    Box(modifier = Modifier.padding(5.dp)) {
        IconButton(onClick = {
            navigateToCart()
        }, content = {
            Icon(
                imageVector = Icons.Default.ShoppingCart, contentDescription = null
            )
        })
        if (uiState is HomeUiState.Success) {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = uiState.cartCount.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .drawBehind {
                            drawCircle(
                                color = Color.LightGray, radius = 40f
                            )
                        },
                )
            }
        }
    }
}

@Composable
internal fun ProductListScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    fabVisibility: (Boolean) -> Unit,
    addToCart: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = uiState.message ?: "Error",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is HomeUiState.Success -> {
                if (uiState.products.isEmpty()) {
                    EmptyScreen()
                    fabVisibility(false)
                } else {
                    fabVisibility(true)
                    ProductGrid(
                        products = uiState.products, addToCart
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>, addToCart: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp)
    ) {
        items(products) { product ->
            ProductItem(
                product = product, addToCart = addToCart
            )
        }
    }
}


@Composable
fun ProductItem(
    product: Product, addToCart: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(190.dp)
                .width(180.dp)
        ) {
            AsyncImage(
                model = product.image,
                modifier = Modifier.height(130.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = product.description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(color = Color.Black, fontSize = 17.sp)
                        )

                        Text(
                            text = "R${product.price}",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(color = Color.Black, fontSize = 20.sp)
                        )
                    }
                    IconButton(modifier = Modifier.weight(1f, false),
                        onClick = { addToCart(product.id) },
                        content = {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = null
                            )
                        })
                }
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Text(
        text = "Sorry no products found", style = TextStyle(color = Color.Blue, fontSize = 16.sp)
    )
}


@Preview
@Composable
fun ComposablePreview() {
    ProductItem(
        product = Product(
            id = "12", description = "ds", image = "", price = 20.0
        )
    ) {

    }
}

@Preview
@Composable
fun CartIconPreview() {
    CartIcon(uiState = HomeUiState.Success(listOf(), 4)) {

    }
}
