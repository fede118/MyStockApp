import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StockGraph(stockData: List<GraphNode>) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm a z")
    val parsedData = stockData.map {
        it.copy(date = LocalDateTime.parse(it.date, dateFormatter).toString())
    }

    val maxPrice = parsedData.maxOf { it.price }
    val minPrice = parsedData.minOf { it.price }
//    val maxVolume = parsedData.maxOf { it.volume }
//    val minVolume = parsedData.minOf { it.volume }

//    val graphWidth = 300.dp
//    val graphHeight = 200.dp
    val padding = 16.dp

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Draw X and Y axes
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding.toPx(), height - padding.toPx()),
            end = androidx.compose.ui.geometry.Offset(width - padding.toPx(), height - padding.toPx()),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding.toPx(), padding.toPx()),
            end = androidx.compose.ui.geometry.Offset(padding.toPx(), height - padding.toPx()),
            strokeWidth = 2f
        )

        // Plot the data points
        val path = Path()
        parsedData.forEachIndexed { index, data ->
            val x = padding.toPx() +
                    (index.toFloat() / (parsedData.size - 1)) * (width - 2 * padding.toPx())
            val y = (height - padding.toPx() -
                    ((data.price - minPrice) / (maxPrice - minPrice)
                    * (height - 2 * padding.toPx()))).toFloat()

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            drawCircle(
                color = Color.Red,
                radius = 4f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }

        // Draw the path connecting the data points
        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 2f)
        )
    }
}

//todo move model
data class GraphNode(val price: Double, val date: String, val volume: Int)

@Preview
@Composable
@Suppress("MagicNumber")
fun StockGraphPreview() {
//    StockGraphTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val stockData = listOf(
                GraphNode(195.3, "Jan 06 2025, 09:30 AM UTC-05:00", 1086),
                GraphNode(196.51, "Jan 06 2025, 09:31 AM UTC-05:00", 174690),
                GraphNode(196.135, "Jan 06 2025, 09:32 AM UTC-05:00", 89155),
                GraphNode(196.075, "Jan 06 2025, 09:33 AM UTC-05:00", 58714),
                GraphNode(196.895, "Jan 06 2025, 09:34 AM UTC-05:00", 66092),
                GraphNode(197.47, "Jan 06 2025, 09:35 AM UTC-05:00", 65901),
                GraphNode(197.5, "Jan 06 2025, 09:36 AM UTC-05:00", 115434),
                GraphNode(197.66, "Jan 06 2025, 09:37 AM UTC-05:00", 60534),
                GraphNode(198.71, "Jan 06 2025, 09:38 AM UTC-05:00", 130239),
                GraphNode(198.68, "Jan 06 2025, 09:39 AM UTC-05:00", 116309)
            )
            StockGraph(stockData)
        }
//    }
}
