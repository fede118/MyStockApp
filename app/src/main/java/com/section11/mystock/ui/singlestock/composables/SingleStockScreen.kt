package com.section11.mystock.ui.singlestock.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.common.composables.MyStockLoader
import com.section11.mystock.ui.common.composables.SmallBodyText
import com.section11.mystock.ui.common.composables.StockCard
import com.section11.mystock.ui.common.events.UiEvent
import com.section11.mystock.ui.common.extentions.showSnackBar
import com.section11.mystock.ui.common.previewsrepositories.FakeRepositoryForPreviews
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel.ClimateChangeScoreUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel.TagUiModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AddToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AlreadyAddedToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.LoadingIcon
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.AddToWatchlistTap
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.RemoveFromWatchlistTap
import com.section11.mystock.ui.singlestock.graph.composables.LineGraph
import com.section11.mystock.ui.theme.Dimens
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.LocalDimens
import com.section11.mystock.ui.theme.LocalSnackbarHostState
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val ICON_ANIMATION_LABEL = "Icon Animation"
private const val ANIM_DURATION = 500

@Composable
fun SingleStockScreen(
    stockInformationUiModel: StockInformationUiModel,
    actionableIconStateFlow: StateFlow<ActionableIconState>,
    graphAnimationEnabled: Boolean = true,
    onEvent: (UiEvent) -> Unit = {}
) {
    val spacing = LocalSpacing.current

    StockCard(
        modifier = Modifier
            .statusBarsPadding()
            .padding(spacing.medium)
    ) {
        SingleStockCardContent(
            stockInformationUiModel = stockInformationUiModel,
            actionableIconStateFlow = actionableIconStateFlow,
            graphAnimationEnabled = graphAnimationEnabled
        ) { event -> onEvent(event) }
    }
}

@Composable
fun SingleStockCardContent(
    modifier: Modifier = Modifier,
    stockInformationUiModel: StockInformationUiModel,
    actionableIconStateFlow: StateFlow<ActionableIconState>,
    graphAnimationEnabled: Boolean = true,
    onEvent: (UiEvent) -> Unit
) {
    with(stockInformationUiModel.summaryUiModel) {
        HeaderSection(stockInformationUiModel, actionableIconStateFlow) { event -> onEvent(event) }
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

        LineGraph(modifier, stockInformationUiModel.graphModel, graphAnimationEnabled)

        StockExtraInfoSection(stockInformationUiModel.knowledgeGraph)
    }
}

@Composable
fun HeaderSection(
    stockInformationUiModel: StockInformationUiModel,
    actionableIconStateFlow: StateFlow<ActionableIconState>,
    onEvent: (UiEvent) -> Unit
) {
    val dimens = LocalDimens.current

    with(stockInformationUiModel.summaryUiModel) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            ActionableIcon(
                modifier = Modifier.align(Alignment.CenterVertically),
                actionableIconStateFlow = actionableIconStateFlow,
                dimens = dimens
            ) { event -> onEvent(event) }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SmallBodyText(text = stockSymbolLabel)
            SmallBodyText(text = exchangeLabel)
        }
    }
}

@Composable
fun ActionableIcon(
    modifier: Modifier = Modifier,
    actionableIconStateFlow: StateFlow<ActionableIconState>,
    dimens: Dimens,
    onEvent: (UiEvent) -> Unit
) {
    val iconState by actionableIconStateFlow.collectAsState()

    AnimatedContent(
        targetState = iconState,
        transitionSpec = {
            fadeIn(animationSpec = tween(ANIM_DURATION)) togetherWith fadeOut(animationSpec = tween(ANIM_DURATION))
        },
        label = ICON_ANIMATION_LABEL
    ) { state ->
        when (state) {
            is AddToWatchlist -> with(state.iconUiModel) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = contentDescription,
                    modifier = modifier
                        .size(dimens.m3)
                        .clickable { onEvent(AddToWatchlistTap(this)) }
                )
            }
            is AlreadyAddedToWatchlist -> with(state.iconUiModel) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = contentDescription,
                    modifier = modifier
                        .size(dimens.m3)
                        .clickable { onEvent(RemoveFromWatchlistTap(this)) }
                )
            }
            is LoadingIcon -> MyStockLoader(modifier.size(dimens.m3))
        }
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

@Composable
fun StockExtraInfoSection(
    knowledgeGraphUiModel: KnowledgeGraphUiModel,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current
    Column(modifier = modifier.fillMaxWidth()) {

        TagsSection(knowledgeGraphUiModel.keyStats.tags, dimens.m1, modifier)

        Spacer(modifier = Modifier.height(dimens.m1Half))

        // Stock Stats
        Column(verticalArrangement = Arrangement.spacedBy(dimens.m1)) {
            knowledgeGraphUiModel.keyStats.stats.forEach { stat ->
                StockStat(label = stat.label, value = stat.value, textSize = dimens.textSmall)
            }
        }

        Spacer(modifier = Modifier.height(dimens.m1Half))

        knowledgeGraphUiModel.keyStats.climateChange?.let {
            ClimateChangeScoreSection(it)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSection(
    tags: List<TagUiModel>,
    spacing: Dp,
    modifier: Modifier = Modifier,
) {
    val arrangement =Arrangement.spacedBy(spacing)
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalArrangement = arrangement
    ) {
        tags.forEach { TagChip(tagUiModel = it) }
    }
}

@Composable
fun TagChip(
    tagUiModel: TagUiModel,
    dimens: Dimens = LocalDimens.current,
    modifier: Modifier = Modifier
) {
    val rememberCoroutineScope = rememberCoroutineScope()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current
    // Todo:
    // showDescription -> tagUiMode.description. long press?

    Box(
        modifier = modifier
            .background(tagUiModel.tagColor, shape = RoundedCornerShape(dimens.m2))
            .clickable {
                tagUiModel.onTapSnackBarModel?.let {
                    rememberCoroutineScope.launch {
                        showSnackBar(tagUiModel.onTapSnackBarModel, snackbarHostState, context)
                    }
                }
            }
            .padding(horizontal = dimens.m1Half, vertical = dimens.mHalf)
    ) {
        Text(text = tagUiModel.text, fontSize = dimens.textVerySmall, color = Color.Black)
    }
}

@Composable
fun StockStat(label: String, value: String, textSize: TextUnit, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = textSize)
        Text(
            text = value,
            fontSize = textSize,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ClimateChangeScoreSection(
    climateChangeScore: ClimateChangeScoreUiModel,
    modifier: Modifier = Modifier,
    dimens: Dimens = LocalDimens.current
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.m1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Replace with actual leaf icon
        Icon(
            painter = painterResource(id = climateChangeScore.iconId),
            contentDescription = climateChangeScore.title,
            tint = Green,
            modifier = Modifier.size(dimens.m2Half)
        )
        Spacer(modifier = Modifier.width(dimens.m1))
        Text(
            text = climateChangeScore.title,
            fontSize = dimens.textSmall
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = climateChangeScore.score,
            fontSize = dimens.textMediumSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Suppress("MagicNumber") // Suppress warning for mock information on the preview
@DarkAndLightPreviews
@Composable
fun StockScreenDarkThemePreview() {
    val fakeRepo = FakeRepositoryForPreviews(LocalContext.current)

    MyStockTheme {
        Surface {
            SingleStockScreen(
                fakeRepo.getSingleStockInformationUiModel(),
                fakeRepo.getActionableIconStateFlow(),
                false
            )
        }
    }
}

