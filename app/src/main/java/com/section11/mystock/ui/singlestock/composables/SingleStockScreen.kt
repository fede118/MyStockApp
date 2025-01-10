package com.section11.mystock.ui.singlestock.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.section11.mystock.ui.singlestock.SingleStockViewModel

@Suppress("ForbiddenComment") // to do comment will be addressed soon
@Composable
fun SingleStockScreen(uiState: SingleStockViewModel.SingleStockUiState.Success) {
    with(uiState.stockInformation.summary) {
        // TODO: Improve this screen and move strings to resource file
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Title: $title")
            Text(text = "Stock: $stock")
            Text(text = "Exchange: $exchange")
            Text(text = "Price: $price")
            Text(text = "Currency: $currency")
            Text(text = "Price Movement Percentage: ${priceMovement.percentage}")
            Text(text = "Price Movement Value: ${priceMovement.value}")
            Text(text = "Price Movement Movement: ${priceMovement.movement}")
        }
    }
}
