package com.section11.mystock.ui.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.ExpandableStockCard
import com.section11.mystock.ui.common.previewsrepositories.FakeRepositoryForPreviews
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.ErrorFetchingSingleStockInfo
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.FetchedSingleStockInfo
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.Idle
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.Loading
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.singlestock.composables.SingleStockCardContent
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme
import kotlinx.coroutines.flow.StateFlow

private const val NO_STOCK_SELECTED = -1
private const val ANIM_DURATION = 300

@Composable
fun StockList(
    modifier: Modifier = Modifier,
    stocks: List<WatchlistStockModel>,
    onStockTap: (WatchlistStockModel) -> Unit,
    singleStockInfoState: StateFlow<SingleStockInformationState>
) {
    var expandedCardIndex by remember { mutableIntStateOf(NO_STOCK_SELECTED) }

    val spacing = LocalSpacing.current
    LazyColumn {
        item { SectionTitle("Your Watchlist") }
        item { Spacer(modifier.height(spacing.small)) }
        items(stocks.size) { index ->
            StockRowItem(
                modifier.padding(spacing.small),
                stock = stocks[index],
                onStockTap = { stockWatchlist ->
                    expandedCardIndex = if (expandedCardIndex == index) NO_STOCK_SELECTED else index
                    onStockTap(stockWatchlist)
                },
                singleStockInfoState = singleStockInfoState,
                isExpanded = index == expandedCardIndex
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(spacing.small)
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(top = spacing.large),
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun StockRowItem(
    modifier: Modifier = Modifier,
    stock: WatchlistStockModel,
    onStockTap: (WatchlistStockModel) -> Unit,
    singleStockInfoState: StateFlow<SingleStockInformationState>,
    isExpanded: Boolean
) {
    ExpandableStockCard(
        modifier = modifier,
        isExpanded = isExpanded,
        onExpandedContentNeeded = { onStockTap(stock) },
        expandedContent = { SingleStockInfoExpandedContent(singleStockInfoState) }
    ) {
        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(tween(ANIM_DURATION)) + expandVertically(
                tween(ANIM_DURATION),
                expandFrom = Alignment.Top
            ),
            exit = fadeOut(tween(ANIM_DURATION)) + shrinkVertically(tween(ANIM_DURATION))
        ) {
            Row {
                Text(
                    text = stock.stockTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "5%", // todo should come from service
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SingleStockInfoExpandedContent(singleStockInfoState: StateFlow<SingleStockInformationState>) {
    val singleStockInfo by singleStockInfoState.collectAsState()
    val spacing = LocalSpacing.current

    when (singleStockInfo) {
        is ErrorFetchingSingleStockInfo -> {
            Text("Error: ${(singleStockInfo as ErrorFetchingSingleStockInfo).message}")
        }
        is FetchedSingleStockInfo -> {
            SingleStockCardContent(
                stockInformationUiModel = (singleStockInfo as FetchedSingleStockInfo).stockInfo
            )
        }
        is Idle -> Spacer(Modifier)
        is Loading -> Box(modifier = Modifier.fillMaxWidth().padding(vertical = spacing.medium)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun HomeScreenStockListPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    MyStockTheme {
        Surface {
            StockList(
                stocks = fakeRepo.getStockWatchlist(),
                onStockTap = {},
                singleStockInfoState = fakeRepo.getSingleStockInfoStateSuccess()
            )
        }
    }
}
