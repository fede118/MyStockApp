package com.section11.mystock.ui.home.search

import androidx.compose.ui.graphics.Color
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.home.search.SearchViewModel.SearchBarUiState
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.model.mapper.StockSearchResultUiMapper
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

private const val SEARCH_RESULT_SIZE = 5

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

    private val stocksInformationUseCase: StocksInformationUseCase = mock()
    private val stockSearchResultUiMapper: StockSearchResultUiMapper = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(
            stocksInformationUseCase,
            stockSearchResultUiMapper,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when search is performed then it should call search on use case`() = runTest {
        whenever(stocksInformationUseCase.searchStock(any())).thenReturn(mock())
        val expectedResult = getSearchResultMockList()
        whenever(stockSearchResultUiMapper.mapToUiModel(any())).thenReturn(expectedResult)

        // When
        searchViewModel.onSearch("query")
        advanceUntilIdle()

        // Then
        assert(searchViewModel.uiState.value is SearchBarUiState.SearchResults)
        with(searchViewModel.uiState.value as SearchBarUiState.SearchResults) {
            assertEquals(expectedResult.size, results.size)
            for(i in expectedResult.indices) {
                assertEquals(expectedResult[i].title, results[i].title)
                assertEquals(expectedResult[i].symbol, results[i].symbol)
                assertEquals(expectedResult[i].symbolBoxColor, results[i].symbolBoxColor)
                assertEquals(expectedResult[i].priceMovementColor, results[i].priceMovementColor)
                assertEquals(expectedResult[i].percentage, results[i].percentage)
            }
        }

    }

    @Test
    fun `when search bar is closed then the Ui state should be IDLE`() {
        // When
        searchViewModel.onSearchBarClosed()

        // Then
        assert(searchViewModel.uiState.value is UiState.Idle)
    }

    @Test
    fun `when search result is tapped then should show snackbar`() {
        searchViewModel.onResultTapped("any result")

        assert(searchViewModel.uiState.value is SearchBarUiState.ShowSnackBar)
    }

    private fun getSearchResultMockList(
        size: Int = SEARCH_RESULT_SIZE
    ): List<StockSearchResultUiModel> {
        return List(size) {
            StockSearchResultUiModel(
                title = "title",
                symbol = "symbol",
                symbolBoxColor = Color.Black,
                priceLabel = "priceLabel",
                priceMovementSymbol = "priceMovementSymbol",
                priceMovementColor = Color.Green,
                percentage = "%"
            )
        }
    }
}
