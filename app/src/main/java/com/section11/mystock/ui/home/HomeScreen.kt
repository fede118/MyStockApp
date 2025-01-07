package com.section11.mystock.ui.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.section11.mystock.models.Stock
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Error
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Success

private val DEFAULT_PADDING = 16.dp

@Composable
fun HomeScreenStockList(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState().also { viewModel.getStocks() }
    when (uiState) {
        is Loading -> FullScreenLoading()
        is Success -> StockList(
            modifier = modifier,
            stocks = (uiState as Success).stocks,
            onStockTap = { stock -> viewModel.onStockTap(stock) }
        )
        is Error -> Text("Error")
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(DEFAULT_PADDING)
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(top = DEFAULT_PADDING),
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun StockList(modifier: Modifier = Modifier, stocks: List<Stock>, onStockTap: (Stock) -> Unit) {
    LazyColumn(modifier = modifier) {
        item { SectionTitle("Your Watchlist") }
        items(stocks.size) { index ->
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
                StockRowItem(stock = stocks[index], onStockTap = onStockTap)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun StockRowItem(modifier: Modifier = Modifier, stock: Stock, onStockTap: (Stock) -> Unit) {
    Row(modifier = modifier.clickable { onStockTap(stock) }) {
        Icon(
            imageVector = Icons.Default.Star,
            modifier = modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            contentDescription = null
        )
        Text(
            text = "${stock.symbol} - ${stock.name}",
            modifier = modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = modifier.weight(1f))
        Text(
            text = "2.5%",
            modifier = modifier.padding(DEFAULT_PADDING)
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
    HomeScreenStockList()
}