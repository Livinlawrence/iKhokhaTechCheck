package com.ikhokha.techcheck.ui.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikhokha.techcheck.domain.model.OrderSummary
import com.livin.ikhokhatechcheck.R

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    orderId: Int,
    summaryViewModel: SummaryViewModel = hiltViewModel(),
    onSendButtonClicked: (OrderSummary) -> Unit
) {
    val orderSummary = summaryViewModel.summaryUiState.orderSummary


    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Order number:#$orderId",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = modifier.padding(10.dp)
                .verticalScroll(rememberScrollState())
                .weight(weight =1f, fill = false)
        ) {
            orderSummary?.products?.forEach { item ->
                Text(
                    text = item.description,
                    fontSize = 15.sp
                )
                Text(
                    text = "Qty: ${item.quantity}",
                    fontSize = 15.sp
                )
                Text(
                    text = "R ${item.price}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider(thickness = 1.dp)
            }
        }
        Text(
            text = "Order date: ${orderSummary?.date} ${orderSummary?.time}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Order total: ${orderSummary?.total}",
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 17.sp,
                color = Color.Red
            )
        )
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { orderSummary?.let { onSendButtonClicked(it) } }) {
            Text(stringResource(R.string.share))
        }
    }
}
