package com.section11.mystock.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.ui.home.HomeUiState
import com.section11.mystock.ui.home.HomeUiState.Loading
import com.section11.mystock.ui.theme.LocalSpacing

@Composable
fun HomeScreenStockList(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onStockTap: (Stock) -> Unit
) {
    when (uiState) {
        is Loading -> FullScreenLoading()
        is HomeUiState.Success -> StockList(
            modifier = modifier,
            stocks = uiState.stocks,
            onStockTap = { stock -> onStockTap(stock) }
        )
        is HomeUiState.Error -> Text("Error")
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(spacing.medium)
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(top = spacing.medium),
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun StockList(modifier: Modifier = Modifier, stocks: List<Stock>, onStockTap: (Stock) -> Unit) {
    val spacing = LocalSpacing.current
    LazyColumn(modifier = modifier) {
        item { SectionTitle("Your Watchlist") }
        items(stocks.size) { index ->
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
                StockRowItem(stock = stocks[index], onStockTap = onStockTap)
                HorizontalDivider(color = Color.Gray, thickness = spacing.minimum)
            }
        }
    }
}

@Composable
fun StockRowItem(modifier: Modifier = Modifier, stock: Stock, onStockTap: (Stock) -> Unit) {
    val spacing = LocalSpacing.current
    Row(modifier = modifier.clickable { onStockTap(stock) }) {
        Icon(
            imageVector = Icons.Default.Star,
            modifier = modifier
                .padding(spacing.small)
                .align(Alignment.CenterVertically),
            contentDescription = null
        )
        Text(
            text = "${stock.symbol} - ${stock.name}",
            modifier = modifier
                .padding(spacing.small)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = modifier.weight(1f))
        Text(
            text = "2.5%",
            modifier = modifier.padding(spacing.medium)
        )
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenStockListPreview() {
    val mockUiState = HomeUiState.Success(listOf(Stock("Apple", "AAPL")))
    HomeScreenStockList(uiState = mockUiState, onStockTap = {})
}
