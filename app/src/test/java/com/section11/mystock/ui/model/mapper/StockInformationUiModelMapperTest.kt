package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation.GraphNode
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.About
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.ClimateChange
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.Stat
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.Tag
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.domain.models.StockInformation.Summary.PriceMovement
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

private const val DEFAULT_DIGITS = 2
private const val STOCK_TITLE = "Apple Inc."
private const val STOCK_SYMBOL = "AAPL"
private const val STOCK_EXCHANGE = "NASDAQ"
private const val STOCK_PRICE = "150.0"
private const val STOCK_CURRENCY = "USD"
private const val PRICE_MOVEMENT_UP = "Up"
private const val PRICE_MOVEMENT_DOWN = "Down"
private const val PRICE_MOVEMENT_VALUE_UP = 10.0
private const val PRICE_MOVEMENT_VALUE_DOWN = 5.0
private const val PRICE_MOVEMENT_PERCENTAGE_UP = 0.5
private const val PRICE_MOVEMENT_PERCENTAGE_DOWN = 0.25
private const val PRICE_MOVEMENT_TITLE = "Price Movement"
private const val DEFAULT_GRAPH_SIZE = 30

class StockInformationUiModelMapperTest {

    private val mockListOfPrices = List(DEFAULT_GRAPH_SIZE) { it.toDouble() }
    private val mockListOfLabels = listOf("Label 1", "Label 2", "Label 3")
    private val mockEdgeLabels = Pair("Label 1", "Label 2")
    private val priceMovement = PriceMovement(
        percentage = PRICE_MOVEMENT_PERCENTAGE_UP,
        value = PRICE_MOVEMENT_VALUE_UP,
        movement = PRICE_MOVEMENT_UP
    )
    private val summary = Summary(
        title = STOCK_TITLE,
        stock = STOCK_SYMBOL,
        exchange = STOCK_EXCHANGE,
        price = STOCK_PRICE,
        currency = STOCK_CURRENCY,
        priceMovement = priceMovement
    )

    private val graphInfo = StockInformation.GraphInformation(
        graphNodes = List(DEFAULT_GRAPH_SIZE) { index ->
            GraphNode(mockListOfPrices[index], "Date $index")
        },
        horizontalAxisLabels = mockListOfLabels,
        edgeLabels = mockEdgeLabels
    )
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var mapper: StockInformationUiModelMapper


    @Before
    fun setup() {
        resourceProvider = mock()
        mapper = StockInformationUiModelMapper(resourceProvider)
        initResourceManagerStrings()
    }

    @Test
    fun `mapToUiModel should map StockInformation to StockInformationUiModel correctly`() {
        // Given
        val stockInformation = StockInformation(
            summary = summary,
            graph = graphInfo,
            knowledgeGraph = mockKnowledgeGraph()
        )

        // When
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Then
        with(uiModel.summaryUiModel) {
            assertEquals(STOCK_TITLE, title)
            assertEquals(STOCK_SYMBOL, stockSymbolLabel)
            assertEquals(STOCK_EXCHANGE, exchangeLabel)
            assertEquals("Price: $STOCK_PRICE $STOCK_CURRENCY", priceLabel)
            assertEquals(PRICE_MOVEMENT_TITLE, priceMovementTitle)
            assertEquals("↑ $PRICE_MOVEMENT_VALUE_UP", priceMovementValueLabel)
            assertEquals("+ $PRICE_MOVEMENT_PERCENTAGE_UP%", priceMovementPercentage)
            assertEquals(Green, priceMovementColor)

        }
        with(uiModel.graphModel) {
            assertEquals(mockListOfPrices.size, graphPoints.size)
            assertEquals(mockListOfPrices, graphPoints)
            assertEquals(mockListOfLabels, graphHorizontalLabels)
        }

        with(uiModel.knowledgeGraph) {
            stockInformation.knowledgeGraph.let {
                assertEquals(it.keyStats.stats.size, keyStats.stats.size)
                it.keyStats.stats.forEachIndexed { index, stat ->
                    assertEquals(stat.label, keyStats.stats[index].label)
                    assertEquals(stat.value, keyStats.stats[index].value)
                    assertEquals(stat.description, keyStats.stats[index].description)
                }
                assertEquals(it.keyStats.tags.size, keyStats.tags.size)
                it.keyStats.tags.forEachIndexed { index, tag ->
                    assertEquals(tag.text, keyStats.tags[index].text)
                    assertEquals(tag.description, keyStats.tags[index].description)
                    assertEquals(tag.link, keyStats.tags[index].link)
                }
            }
            keyStats.climateChange.let {
                assertEquals(it.score, keyStats.climateChange.score)
                assertEquals(it.link, keyStats.climateChange.link)
            }
        }
    }

    @Test
    fun `mapToUiModel should handle price movement Up correctly`() {
        // Given
        val priceMovement = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE_UP,
            value = PRICE_MOVEMENT_VALUE_UP,
            movement = PRICE_MOVEMENT_UP
        )
        val summary = Summary(
            title = STOCK_TITLE,
            stock = STOCK_SYMBOL,
            exchange = STOCK_EXCHANGE,
            price = STOCK_PRICE,
            currency = STOCK_CURRENCY,
            priceMovement = priceMovement
        )
        val stockInformation = StockInformation(
            summary = summary,
            graph = mock(), // graph mapping is already tested in first test
            knowledgeGraph = mockKnowledgeGraph()
        )

        // When
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Then
        with(uiModel.summaryUiModel) {
            assertEquals("↑ $PRICE_MOVEMENT_VALUE_UP", priceMovementValueLabel)
            assertEquals("+ $PRICE_MOVEMENT_PERCENTAGE_UP%", priceMovementPercentage)
            assertEquals(Green, priceMovementColor)
        }
    }

    @Test
    fun `mapToUiModel should handle price movement Down correctly`() {
        // Given
        val priceMovement = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE_DOWN,
            value = PRICE_MOVEMENT_VALUE_DOWN,
            movement = PRICE_MOVEMENT_DOWN
        )
        val summary = Summary(
            title = STOCK_TITLE,
            stock = STOCK_SYMBOL,
            exchange = STOCK_EXCHANGE,
            price = STOCK_PRICE,
            currency = STOCK_CURRENCY,
            priceMovement = priceMovement
        )
        val stockInformation = StockInformation(
            summary = summary,
            graph = mock(), // graph mapping is already tested in first test
            knowledgeGraph = mockKnowledgeGraph()
        )

        // When
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Then
        with(uiModel.summaryUiModel) {
            assertEquals("↓ $PRICE_MOVEMENT_VALUE_DOWN", priceMovementValueLabel)
            assertEquals("- $PRICE_MOVEMENT_PERCENTAGE_DOWN%", priceMovementPercentage)
            assertEquals(Red, priceMovementColor)
        }
    }

    private fun initResourceManagerStrings() {
        whenever(resourceProvider.getString(R.string.single_stock_screen_symbol_text, STOCK_SYMBOL))
            .thenReturn(STOCK_SYMBOL)
        whenever(resourceProvider.getString(R.string.single_stock_screen_exchange_text, STOCK_EXCHANGE))
            .thenReturn(STOCK_EXCHANGE)
        whenever(resourceProvider.getString(R.string.single_stock_screen_price_text, STOCK_CURRENCY, STOCK_PRICE))
            .thenReturn("Price: $STOCK_PRICE $STOCK_CURRENCY")
        whenever(resourceProvider.getString(R.string.single_stock_screen_price_movement_title))
            .thenReturn(PRICE_MOVEMENT_TITLE)
        whenever(resourceProvider.getString(R.string.single_stock_screen_arrow_up))
            .thenReturn("↑")
        whenever(resourceProvider.getString(R.string.single_stock_screen_arrow_down))
            .thenReturn("↓")
        whenever(
            resourceProvider.getString(
                eq(R.string.single_stock_screen_price_movement_label),
                eq("↑"),
                any()
            )
        ).thenReturn("↑ $PRICE_MOVEMENT_VALUE_UP")
        whenever(
            resourceProvider.getString(
                eq(R.string.single_stock_screen_price_movement_label),
                eq("↓"),
                any()
            )
        ).thenReturn("↓ $PRICE_MOVEMENT_VALUE_DOWN")
        whenever(resourceProvider.getString(R.string.single_stock_screen_plus))
            .thenReturn("+")
        whenever(resourceProvider.getString(R.string.single_stock_screen_minus))
            .thenReturn("-")
        whenever(
            resourceProvider.getString(
                R.string.single_stock_screen_price_movement_percentage,
                "+",
                PRICE_MOVEMENT_PERCENTAGE_UP.format(DEFAULT_DIGITS)
            )
        ).thenReturn("+ $PRICE_MOVEMENT_PERCENTAGE_UP%")
        whenever(
            resourceProvider.getString(
                R.string.single_stock_screen_price_movement_percentage,
                "-",
                PRICE_MOVEMENT_PERCENTAGE_DOWN.format(DEFAULT_DIGITS)
            )
        ).thenReturn("- $PRICE_MOVEMENT_PERCENTAGE_DOWN%")
        whenever(resourceProvider.getString(R.string.single_stock_screen_climate_score_title))
            .thenReturn("CDP Climate Change Score")
        whenever(
            resourceProvider.getString(R.string.single_stock_screen_snackbar_with_link_title, "link")
        ).thenReturn("You're about to open an external link: link")
        whenever(
            resourceProvider.getString(R.string.single_stock_screen_snackbar_with_link_action_label, "link")
        ).thenReturn("Open Link")
    }

    private fun mockKnowledgeGraph(): KnowledgeGraph {
        val mockStat = mock<Stat>()
        with(mockStat) {
            whenever(label).thenReturn("label")
            whenever(value).thenReturn("10.0")
            whenever(description).thenReturn("Description")
        }

        val mockTag = mock<Tag>()
        with(mockTag) {
            whenever(text).thenReturn("text")
            whenever(description).thenReturn("description")
            whenever(link).thenReturn("link")

        }

        val mockClimateChange = mock<ClimateChange>()
        with(mockClimateChange) {
            whenever(score).thenReturn("10.0")
            whenever(link).thenReturn("link")
        }

        val mockKeyStats = mock<KeyStats>()
        with(mockKeyStats) {
            whenever(stats).thenReturn(listOf(mockStat))
            whenever(tags).thenReturn(listOf(mockTag))
            whenever(climateChange).thenReturn(mockClimateChange)
        }

        val mockInfo = mock<About.Info>()
        with(mockInfo) {
            whenever(label).thenReturn("label")
            whenever(value).thenReturn("value")
            whenever(link).thenReturn("link")
        }

        val mockDescription = mock<About.Description>()
        with(mockDescription) {
            whenever(snippet).thenReturn("snippet")
            whenever(link).thenReturn("link")
            whenever(linkText).thenReturn("linkText")
        }

        val mockAbout = mock<About>()
        with(mockAbout) {
            whenever(title).thenReturn("title")
            whenever(description).thenReturn(mockDescription)
            whenever(info).thenReturn(listOf(mockInfo))
        }

        val knowledgeGraph = mock<KnowledgeGraph>()
        with(knowledgeGraph) {
            whenever(about).thenReturn(listOf(mockAbout))
            whenever(keyStats).thenReturn(mockKeyStats)
        }

        return knowledgeGraph
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)
