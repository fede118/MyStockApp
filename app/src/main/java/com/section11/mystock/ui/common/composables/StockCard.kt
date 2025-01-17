package com.section11.mystock.ui.common.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun StockCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val spacing = LocalSpacing.current

    Box(modifier = modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(spacing.medium),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = spacing.smallest)
        ) {
            Column(
                modifier = Modifier.padding(spacing.medium),
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ExpandableStockCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpandedContentNeeded: () -> Unit,
    expandedContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    StockCard(
        modifier = modifier
            .animateContentSize()
            .clickable {
                onExpandedContentNeeded()
            }
    ) {
        content()
        if (isExpanded) {
            expandedContent()
        }
    }
}

@DarkAndLightPreviews
@Composable
fun StockCardPreview() {
    MyStockTheme {
        Surface {
            StockCard(modifier = Modifier.statusBarsPadding()) {
                Text("Testing - Testing")
            }
        }
    }
}

/**
 * Use interactive mode to see expanded card
 */
@DarkAndLightPreviews
@Composable
fun ExpandableStockCardPreview() {
    var isExpanded by remember { mutableStateOf(false) }

    MyStockTheme {
        Surface {
            ExpandableStockCard(
                modifier = Modifier.statusBarsPadding(),
                isExpanded = isExpanded,
                onExpandedContentNeeded = { isExpanded = !isExpanded },
                expandedContent = {
                    Text(
                        text = "Expanded Content Here",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            ) {
                Text("This is the title")
            }
        }
    }
}
