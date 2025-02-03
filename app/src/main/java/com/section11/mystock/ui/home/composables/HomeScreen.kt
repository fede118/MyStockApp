package com.section11.mystock.ui.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.ExpandableStockCard
import com.section11.mystock.ui.common.composables.MyStockLoader
import com.section11.mystock.ui.common.events.UiEvent
import com.section11.mystock.ui.common.previewsrepositories.FakeRepositoryForPreviews
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Idle
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
import com.section11.mystock.ui.singlestock.composables.SingleStockCardContent
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val NO_STOCK_SELECTED = -1
private const val ANIM_DURATION = 300

@Composable
fun WatchlistScreen(
    modifier: Modifier = Modifier,
    stocksScreenUiModel: WatchlistScreenUiModel,
    onEvent: (UiEvent) -> Unit,
    singleStockInfoState: StateFlow<UiState>,
    singleStockActionableItemStateFlow: StateFlow<ActionableIconState>,
    searchUiStateFlow: StateFlow<UiState>
) {
    val spacing = LocalSpacing.current

    Column {
        StocksSearchBar(
            hint = stocksScreenUiModel.searchHint,
            onEvent = onEvent,
            searchUiStateFlow = searchUiStateFlow,
            modifier = modifier
                .align(CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = spacing.medium)
        )
        StockList(
            modifier = modifier.padding(horizontal = spacing.medium),
            stocksScreenUiModel = stocksScreenUiModel,
            onEvent = onEvent,
            singleStockInfo = singleStockInfoState,
            singleStockActionableItemStateFlow = singleStockActionableItemStateFlow
        )
    }
}

@Composable
fun StockList(
    modifier: Modifier = Modifier,
    stocksScreenUiModel: WatchlistScreenUiModel,
    onEvent: (UiEvent) -> Unit,
    singleStockInfo: StateFlow<UiState>,
    singleStockActionableItemStateFlow: StateFlow<ActionableIconState>
) {
    var expandedCardIndex by remember { mutableIntStateOf(NO_STOCK_SELECTED) }

    val spacing = LocalSpacing.current
    LazyColumn {
        item { Spacer(modifier.height(spacing.small)) }
        items(stocksScreenUiModel.stocks.size) { index ->
            StockRowItem(
                modifier.padding(vertical = spacing.small),
                stock = stocksScreenUiModel.stocks[index],
                singleStockInfoState = singleStockInfo,
                singleStockActionableItemStateFlow = singleStockActionableItemStateFlow,
                isExpanded = index == expandedCardIndex
            ) { event ->
                if (event is WatchlistScreenEvent.StockTapped) {
                    expandedCardIndex = if (expandedCardIndex == index) NO_STOCK_SELECTED else index
                }
                onEvent(event)
            }
        }
    }
}

@Composable
fun StockRowItem(
    modifier: Modifier = Modifier,
    stock: WatchlistStockModel,
    singleStockInfoState: StateFlow<UiState>,
    singleStockActionableItemStateFlow: StateFlow<ActionableIconState>,
    isExpanded: Boolean,
    onEvent: (UiEvent) -> Unit
) {
    ExpandableStockCard(
        modifier = modifier,
        isExpanded = isExpanded,
        onExpandedContentNeeded = { onEvent(WatchlistScreenEvent.StockTapped(stock)) },
        expandedContent = {
            SingleStockInfoExpandedContent(
                singleStockInfoState,
                singleStockActionableItemStateFlow
            ) { event -> onEvent(event) }
        }
    ) {
        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(
                tween(ANIM_DURATION)) + expandVertically(tween(ANIM_DURATION),
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
                        text = stock.percentageChange,
                        style = MaterialTheme.typography.bodyLarge,
                        color = stock.percentageColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SingleStockInfoExpandedContent(
    singleStockUiState: StateFlow<UiState>,
    actionableIconStateFlow: StateFlow<ActionableIconState>,
    onEvent: (UiEvent) -> Unit
) {
    val singleStockInfo by singleStockUiState.collectAsState()
    when (singleStockInfo) {
        is UiState.Error -> Text("Error: ${(singleStockInfo as UiState.Error).message}")
        is SingleStockFetched -> SingleStockCardContent(
            stockInformationUiModel = (singleStockInfo as SingleStockFetched).stockInformationUiModel,
            actionableIconStateFlow = actionableIconStateFlow
        ) { event -> onEvent(event) }
        is Idle -> @Composable {}
        is Loading -> MyStockLoader(Modifier.fillMaxWidth())
    }
}

@DarkAndLightPreviews
@Composable
fun HomeScreenStockListPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    MyStockTheme {
        Surface {
            WatchlistScreen(
                stocksScreenUiModel = fakeRepo.getStockWatchlist(),
                onEvent = {},
                singleStockInfoState = fakeRepo.getSingleStockInfoStateSuccess(),
                singleStockActionableItemStateFlow = fakeRepo.getActionableIconStateFlow(),
                searchUiStateFlow = MutableStateFlow(Idle)
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun HomeScreenEmptyPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    MyStockTheme {
        Surface {
            WatchlistScreen(
                stocksScreenUiModel = fakeRepo.getEmptyList(),
                onEvent = {},
                singleStockInfoState = fakeRepo.getSingleStockInfoStateSuccess(),
                singleStockActionableItemStateFlow = fakeRepo.getActionableIconStateFlow(),
                searchUiStateFlow = MutableStateFlow(Idle)
            )
        }
    }
}
