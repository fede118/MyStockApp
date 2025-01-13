package com.section11.mystock.ui.singlestock.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.SmallBodyText
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun SingleStockScreen(uiState: SingleStockViewModel.SingleStockUiState.Success) {
    with(uiState.stockInformationUiModel) {
        val spacing = LocalSpacing.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.medium)
                .statusBarsPadding()
        ) {
            Card(
                shape = RoundedCornerShape(spacing.medium),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.minimum)
            ) {
                Column(
                    modifier = Modifier.padding(spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(spacing.small)
                ) {
                    HeaderSection(title, stockSymbolLabel, exchangeLabel)
                    Text(
                        text = priceLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    PriceMovementSection(
                        priceMovementTitle = priceMovementTitle,
                        priceMovementValueLabel = priceMovementValueLabel,
                        priceMovementColor = priceMovementColor,
                        priceMovementPercentage = priceMovementPercentage
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderSection(title: String, stockSymbolLabel: String, exchangeLabel: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        SmallBodyText(text = stockSymbolLabel)
        SmallBodyText(text = exchangeLabel)
    }
}

@Composable
fun PriceMovementSection(
    priceMovementTitle: String,
    priceMovementValueLabel: String,
    priceMovementColor: Color,
    priceMovementPercentage: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = priceMovementTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = priceMovementValueLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = priceMovementColor,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = priceMovementPercentage,
                style = MaterialTheme.typography.bodyLarge,
                color = priceMovementColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@DarkAndLightPreviews()
@Composable
fun StockScreenDarkThemePreview() {
    val stockInformationUiModel = StockInformationUiModel(
        title = "Apple Inc.",
        stockSymbolLabel = "Symbol: AAPL",
        exchangeLabel = "Exchange: NASDAQ",
        priceLabel = "Price: 150.0",
        priceMovementTitle = "Price Movement",
        priceMovementPercentage = "+5.0%",
        priceMovementColor = Green,
        priceMovementValueLabel = "+10.0"
    )

    val uiState = SingleStockViewModel.SingleStockUiState.Success(stockInformationUiModel)
    MyStockTheme {
        Surface {
            SingleStockScreen(uiState)
        }
    }
}

