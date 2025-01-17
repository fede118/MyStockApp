package com.section11.mystock.ui.singlestock.graph.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.model.GraphUiModel
import com.section11.mystock.ui.theme.Green40
import com.section11.mystock.ui.theme.LocalDimens
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme

private const val ZERO_F = 0f
private const val ZERO = 0
private const val GRAPH_NODE_PADDING_MULTIPLIER = 0.01f
private const val ONE = 1
private const val FIRST_INDEX = 0
private const val INITIAL_ANIM_VALUE = 0f
private const val TARGET_ANIM_VALUE = 1f
private const val ANIM_DURATION = 3000
private const val GRAPH_ASPECT_RATIO = 3 / 2f

/**
 * Generates a graph with the provided [graphUiModel].
 *
 * @param graphUiModel the [GraphUiModel] representing the information drawn in the graph
 * @param animationEnabled is the animation of the graph enabled or not. Disable for Previews of the
 * graph to work
 */
@Composable
fun LineGraph(graphUiModel: GraphUiModel, animationEnabled: Boolean = true) {
    val textMeasurer = rememberTextMeasurer()
    val spacing = LocalSpacing.current
    val dimens = LocalDimens.current
    val animateProgress = remember {
        Animatable(INITIAL_ANIM_VALUE)
    }
    val backgroundVerticalLines = graphUiModel.graphBackgroundVerticalLinesAmount ?: ZERO
    val backgroundHorizontalLines = graphUiModel.graphBackgroundHorizontalLinesAmount ?: ZERO

    LaunchedEffect(key1 = graphUiModel.graphPoints) {
        animateProgress.animateTo(TARGET_ANIM_VALUE, tween(ANIM_DURATION))
    }

    Box(
        modifier = Modifier
            .padding(spacing.small)
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .padding(spacing.small)
                .aspectRatio(GRAPH_ASPECT_RATIO)
                .fillMaxWidth()
                .drawWithCache {
                    val path = generatePath(graphUiModel.graphPoints, size)
                    val filledPath = Path().apply {
                        addPath(path)
                        lineTo(size.width, size.height)
                        lineTo(ZERO_F, size.height)
                        close()
                    }

                    onDrawBehind {
                        drawBackgroundGrid(
                            Pair(backgroundVerticalLines, backgroundHorizontalLines),
                            graphHorizontalLabels = graphUiModel.graphHorizontalLabels,
                            graphHorizontalLabelsPaddingTop = spacing.verySmall.toPx(),
                            stokeSize = dimens.smallest.toPx(),
                            textSize = dimens.textMedium,
                            textMeasurer = textMeasurer
                        )


                        val widthValue = if (animationEnabled) {
                            size.width * animateProgress.value
                        } else {
                            size.width
                        }
                        clipRect(right = widthValue) {
                            val brush = Brush.verticalGradient(listOf(Green40, Color.Transparent))
                            drawPath(path, Color.Green, style = Stroke(dimens.verySmall.toPx()))
                            drawPath(
                                path = filledPath,
                                brush = brush,
                                style = Fill
                            )
                        }
                    }
                }
        )
    }
}

private fun generatePath(data: List<Double>, size: Size): Path {
    val path = Path()
    val numberOfEntries = data.size - ONE
    val xWidth = size.width / numberOfEntries
    // we don't wont the max or min value to go to the edge of the graph:
    val padding = data.average().toFloat() * GRAPH_NODE_PADDING_MULTIPLIER

    val maxValue = data.maxBy { it } + padding
    val minValue = data.minBy { it } - padding
    val range =  maxValue - minValue
    val heightPxPerAmount = size.height / range.toFloat()

    data.forEachIndexed { index, value ->
        if (index == FIRST_INDEX) {
            path.moveTo(
                ZERO_F,
                (size.height - (value - minValue).toFloat() * heightPxPerAmount)
            )
        }

        val valueX = index * xWidth
        val valueY = size.height - (value - minValue).toFloat() * heightPxPerAmount
        path.lineTo(valueX, valueY)
    }

    return path
}

private fun DrawScope.drawBackgroundGrid(
    verticalAndHorizontalLines: Pair<Int, Int>,
    graphHorizontalLabels: List<String>? = null,
    graphHorizontalLabelsPaddingTop: Float,
    stokeSize: Float,
    textSize: TextUnit,
    textMeasurer: TextMeasurer
) {
    drawRect(Color.Gray, style = Stroke(stokeSize))
    val (backgroundVerticalLines: Int, backgroundHorizontalLines: Int) = verticalAndHorizontalLines

    drawVerticalLines(
        backgroundVerticalLines = backgroundVerticalLines,
        barWidthPx = stokeSize,
        graphHorizontalLabels = graphHorizontalLabels,
        textSize = textSize,
        textMeasurer = textMeasurer,
        paddingTop = graphHorizontalLabelsPaddingTop
    )

    drawHorizontalLines(
        backgroundHorizontalLines = backgroundHorizontalLines,
        barWidthPx = stokeSize
    )
}

private fun DrawScope.drawVerticalLines(
    backgroundVerticalLines: Int,
    barWidthPx: Float,
    textSize: TextUnit,
    textMeasurer: TextMeasurer,
    paddingTop: Float,
    graphHorizontalLabels: List<String>? = null
) {
    val verticalSize = size.width / (backgroundVerticalLines + ONE)
    repeat(backgroundVerticalLines) { i ->
        val startX = verticalSize * (i + ONE)
        drawLine(
            Color.Gray,
            start = Offset(startX, ZERO_F),
            end = Offset(startX, size.height),
            strokeWidth = barWidthPx
        )

        graphHorizontalLabels?.let {
            drawXAxisLabelsCentered(
                text = it[i],
                textSize = textSize,
                x = startX,
                textMeasurer = textMeasurer,
                paddingTop = paddingTop
            )
        }
    }
}

private fun DrawScope.drawXAxisLabelsCentered(
    text: String,
    textSize: TextUnit,
    x: Float,
    textMeasurer: TextMeasurer,
    paddingTop: Float
) {
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = textSize,
        fontWeight = FontWeight.Normal
    )
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = textStyle
    )

    val textWidth = textLayoutResult.size.width.toFloat()
    val centeredX = x - (textWidth / 2)

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(centeredX, size.height + paddingTop)
    )
}

private fun DrawScope.drawHorizontalLines(backgroundHorizontalLines: Int, barWidthPx: Float) {
    val sectionSize = size.height / (backgroundHorizontalLines + ONE)
    repeat(backgroundHorizontalLines) { i ->
        val startY = sectionSize * (i + ONE)
        drawLine(
            Color.Gray,
            start = Offset(ZERO_F, startY),
            end = Offset(size.width, startY),
            strokeWidth = barWidthPx
        )
    }
}

@Suppress("MagicNumber") // Suppress warning for mock information on the preview
@DarkAndLightPreviews
@Composable
fun MyScreen() {
    MyStockTheme {
        Surface {
            val dataPoints = listOf(
                100.00,
                120.05,
                115.15,
                129.15,
                156.15,
                110.15,
                300.12
                // Add more data points here...
            )
            val graphInfo = GraphUiModel(
                graphPoints = dataPoints,
                graphHorizontalLabels = listOf("08:31", "08:36", "08:40", "08:45")
            )
            LineGraph(graphInfo, animationEnabled = false)
        }
    }
}
