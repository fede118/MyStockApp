package com.section11.mystock.ui.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchBarClosed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchPerformed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchResultTapped
import com.section11.mystock.ui.home.search.SearchViewModel.SearchUiState
import com.section11.mystock.ui.home.search.SearchViewModel.SearchUiState.SearchResults
import com.section11.mystock.ui.theme.LocalSpacing
import kotlinx.coroutines.flow.StateFlow

private const val EMPTY_STRING = ""
private const val CLEAR_QUERY_DESCRIPTION = "Clear search"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksSearchBar(
    hint: String,
    onEvent: (WatchlistScreenEvent) -> Unit,
    searchUiStateFlow: StateFlow<SearchUiState>,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf(EMPTY_STRING) }
    var isActive by remember { mutableStateOf(false) }
    val searchUiState by searchUiStateFlow.collectAsState()
    val searchResults = (searchUiState as? SearchResults)?.results

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
            searchResults?.let {
                SearchResultsList(it) { tappedResult ->
                    // collapse the searchbar
                    isActive = false
                    query = EMPTY_STRING
                    onEvent(SearchResultTapped(tappedResult))
                }
            }
        }
    }
}

@Composable
fun SearchResultsList(searchResults: List<String>, onResultTap: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(searchResults.size) { index ->
            SearchResultItem(
                result = searchResults[index],
                onClick = {
                    // Handle result click
                    onResultTap(searchResults[index])
                     // Collapse the search bar
                }
            )
        }
    }
}

@Composable
fun SearchResultItem(result: String, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    Text(
        text = result,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(spacing.medium),
        style = MaterialTheme.typography.bodyMedium
    )
}
