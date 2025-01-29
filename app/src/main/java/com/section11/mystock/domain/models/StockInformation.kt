package com.section11.mystock.domain.models

data class StockInformation(
    val summary: Summary,
    val graph: GraphInformation,
    val knowledgeGraph: KnowledgeGraph
) {
    data class Summary(
        val title: String,
        val stock: String,
        val exchange: String,
        val price: String,
        val currency: String,
        val priceMovement: PriceMovement
    ) {
        data class PriceMovement(
            val percentage: Double,
            val value: Double,
            val movement: String
        )
    }

    /**
     * Represents the graph information.
     *
     * @property graphNodes are simply just the nodes on the graph
     * @property edgeLabels are the labels to be shown on the edges of the graph at X. So at the
     * start and the end of the graph, meaning the starting and finish points.
     * @property horizontalAxisLabels are the labels to be shown at the bottom of the graph, under each
     * inner line of the background grid. Because the edges are already taken care of by [graphEdgeLabels]
     */
    data class GraphInformation(
        val graphNodes: List<GraphNode>,
        val edgeLabels: Pair<String, String>,
        val horizontalAxisLabels: List<String>
    ) {
        data class GraphNode(
            val price: Double,
            val dateLabel: String
        )

        companion object {
            const val SERVICE_RESPONSE_DATE_FORMAT = "MMM dd yyyy, hh:mm a 'UTC'XXX"
            const val GRAPH_NODE_DATE_FORMAT = "HH:mm"
            const val GRAPH_DEFAULT_HORIZONTAL_LABELS = 4
        }
    }

    data class KnowledgeGraph(
        val keyStats: KeyStats,
        val about: List<About>
    ) {
        data class KeyStats(
            val tags: List<Tag>,
            val stats: List<Stat>,
            val climateChange: ClimateChange
        ) {
            data class Tag(
                val text: String,
                val description: String,
                val link: String? = null
            )

            data class Stat(
                val label: String,
                val description: String,
                val value: String
            )

            data class ClimateChange(
                val score: String,
                val link: String
            )
        }

        data class About(
            val title: String,
            val description: Description,
            val info: List<Info>
        ) {
            data class Description(
                val snippet: String,
                val link: String,
                val linkText: String
            )

            data class Info(
                val label: String,
                val value: String,
                val link: String? = null
            )
        }
    }
}
