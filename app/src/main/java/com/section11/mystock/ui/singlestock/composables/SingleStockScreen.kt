package com.section11.mystock.ui.singlestock.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.SmallBodyText
import com.section11.mystock.ui.common.composables.StockCard
import com.section11.mystock.ui.common.previewsrepositories.FakeRepositoryForPreviews
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.singlestock.graph.composables.LineGraph
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun SingleStockScreen(
    stockInformationUiModel: StockInformationUiModel,
    graphAnimationEnabled: Boolean = true
) {
    val spacing = LocalSpacing.current

    StockCard(
        modifier = Modifier
            .statusBarsPadding()
            .padding(spacing.medium)
    ) {
        SingleStockCardContent(
            stockInformationUiModel = stockInformationUiModel,
            graphAnimationEnabled = graphAnimationEnabled
        )
    }
}

@Composable
fun SingleStockCardContent(
    modifier: Modifier = Modifier,
    stockInformationUiModel: StockInformationUiModel,
    graphAnimationEnabled: Boolean = true
) {
    with(stockInformationUiModel) {
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

        LineGraph(modifier, graphModel, graphAnimationEnabled)
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

@Suppress("MagicNumber") // Suppress warning for mock information on the preview
@DarkAndLightPreviews
@Composable
fun StockScreenDarkThemePreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)

    MyStockTheme {
        Surface {
            SingleStockScreen(fakeRepo.getSingleStockInformationUiModel(), false)
        }
    }
}

