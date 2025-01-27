package com.section11.mystock.ui.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.MyStockLoader
import com.section11.mystock.ui.common.previewsrepositories.FakeRepositoryForPreviews
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchBarClosed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchPerformed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchResultTapped
import com.section11.mystock.ui.home.search.SearchViewModel.SearchBarUiState.SearchResults
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.theme.LocalDimens
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val EMPTY_STRING = ""
private const val CLEAR_QUERY_DESCRIPTION = "Clear search"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksSearchBar(
    hint: String,
    onEvent: (WatchlistScreenEvent) -> Unit,
    searchUiStateFlow: StateFlow<UiState>,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf(EMPTY_STRING) }
    var isActive by remember { mutableStateOf(false) }
    val searchUiState by searchUiStateFlow.collectAsState()

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = { query = it },
        onSearch = { onEvent(SearchPerformed(query)) },
        active = isActive,
        onActiveChange = {
            isActive = it
            if (!isActive) {
                onEvent(SearchBarClosed)
                query = EMPTY_STRING
            }
        },
        placeholder = { Text(hint) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = EMPTY_STRING }) {
                    Icon(Icons.Default.Close, contentDescription = CLEAR_QUERY_DESCRIPTION)
                }
            }
        }
    ) {
        if (isActive) {
            when(searchUiState) {
                is UiState.Error -> Text("Error During Search")
                is UiState.Loading -> MyStockLoader()
                is SearchResults -> {
                    (searchUiState as? SearchResults)?.results?.let {
                        SearchResultsList(it) { tappedResult ->
                            // collapse the searchbar
                            isActive = false
                            query = EMPTY_STRING
                            onEvent(SearchResultTapped(tappedResult))
                        }
                    } ?: Text("Error Showing Results")
                }
                else -> @Composable {} // The snackBar is handled upstream in the Scaffold and Idle means doing nothing
            }
        }
    }
}

@Composable
fun SearchResultsList(
    searchResults: List<StockSearchResultUiModel>,
    onResultTap: (StockSearchResultUiModel) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(searchResults.size) { index ->
            SearchResultItem(
                result = searchResults[index],
                onClick = { onResultTap(searchResults[index]) }
            )
        }
    }
}

@Composable
fun SearchResultItem(
    result: StockSearchResultUiModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val dimens = LocalDimens.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.medium)
            .clickable { onClick(result.symbolColonExchange) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.symbolColonExchange,
                style = MaterialTheme.typography.bodyMedium,
                color = result.symbolBoxColor
            )
            Text(
                text = result.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = result.priceLabel,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = spacing.small)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = result.priceMovementSymbol,
                fontSize = dimens.textMediumSmall,
                color = result.priceMovementColor,
                modifier = Modifier.padding(end = spacing.extraSmall).alignByBaseline()
            )
            Text(
                text = result.percentage,
                fontSize = dimens.textSmall,
                color = result.priceMovementColor
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun SearchBarWithExactMatchPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    val results = fakeRepo.getSearchResultsWithExactMatch()
    val uiState = remember { MutableStateFlow(SearchResults(results)) }
    MyStockTheme {
        Surface {
            StocksSearchBar(
                hint = "Search",
                onEvent = {},
                searchUiStateFlow = uiState
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun SearchResultItemPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    val results = fakeRepo.getSearchResultsWithExactMatch()
    MyStockTheme {
        Surface {
            SearchResultItem(
                result = results.first(),
                onClick = {},
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun SearchMultipleResultsItemPreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)
    val results = fakeRepo.getSearchResultWithNoExactMatch()
    MyStockTheme {
        Surface {
            SearchResultItem(
                result = results.first(),
                onClick = {},
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}
