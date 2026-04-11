package com.finflow.app.ui.composable.main.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.ui.model.MonthStatUiModel
import java.text.DecimalFormat

@Composable
fun IncomeLineChart(
    monthStats: List<MonthStatUiModel>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textMeasurer = rememberTextMeasurer()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_income_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (monthStats.isEmpty()) return@Canvas

                val leftPadding = 80f
                val rightPadding = 24f
                val topPadding = 16f
                val bottomPadding = 40f

                val chartWidth = size.width - leftPadding - rightPadding
                val chartHeight = size.height - topPadding - bottomPadding

                val maxIncome = monthStats.maxOf { it.incomeTotal }
                val minIncome = monthStats.minOf { it.incomeTotal }
                val range = if (maxIncome == minIncome) maxIncome.toFloat().coerceAtLeast(1f)
                else (maxIncome - minIncome).toFloat()
                val yMin = if (maxIncome == minIncome) 0L else minIncome - (range * 0.1f).toLong()
                val yMax = maxIncome + (range * 0.1f).toLong()
                val yRange = (yMax - yMin).toFloat().coerceAtLeast(1f)

                // Y axis grid lines (3 lines)
                val gridSteps = 3
                for (i in 0..gridSteps) {
                    val y = topPadding + chartHeight * (1f - i.toFloat() / gridSteps)
                    drawLine(
                        color = gridColor,
                        start = Offset(leftPadding, y),
                        end = Offset(size.width - rightPadding, y),
                        strokeWidth = 1f
                    )
                    val value = yMin + (yRange * i / gridSteps).toLong()
                    val label = formatAmount(value)
                    val textResult = textMeasurer.measure(
                        text = label,
                        style = TextStyle(fontSize = 10.sp, color = labelColor)
                    )
                    drawText(
                        textLayoutResult = textResult,
                        topLeft = Offset(
                            leftPadding - textResult.size.width - 8f,
                            y - textResult.size.height / 2f
                        )
                    )
                }

                // Plot points
                val points = monthStats.mapIndexed { index, stat ->
                    val x = leftPadding + chartWidth * index / (monthStats.size - 1).coerceAtLeast(1)
                    val y = topPadding + chartHeight * (1f - (stat.incomeTotal - yMin) / yRange)
                    Offset(x, y)
                }

                // Draw line
                if (points.size > 1) {
                    val path = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            lineTo(points[i].x, points[i].y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = 3f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                // Draw points
                points.forEach { point ->
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = point
                    )
                    drawCircle(
                        color = pointColor,
                        radius = 4f,
                        center = point
                    )
                }

                // X axis labels
                drawMonthLabels(
                    monthStats = monthStats,
                    textMeasurer = textMeasurer,
                    leftPadding = leftPadding,
                    chartWidth = chartWidth,
                    y = size.height - 4f,
                    labelColor = labelColor
                )
            }
        }
    }
}

private fun DrawScope.drawMonthLabels(
    monthStats: List<MonthStatUiModel>,
    textMeasurer: TextMeasurer,
    leftPadding: Float,
    chartWidth: Float,
    y: Float,
    labelColor: Color
) {
    monthStats.forEachIndexed { index, stat ->
        val x = leftPadding + chartWidth * index / (monthStats.size - 1).coerceAtLeast(1)
        val textResult = textMeasurer.measure(
            text = stat.monthLabel,
            style = TextStyle(fontSize = 10.sp, color = labelColor)
        )
        drawText(
            textLayoutResult = textResult,
            topLeft = Offset(
                x - textResult.size.width / 2f,
                y - textResult.size.height
            )
        )
    }
}

private fun formatAmount(amount: Long): String {
    val formatter = DecimalFormat("#,###")
    return when {
        amount >= 10_000_000 -> "${formatter.format(amount / 10_000)}만"
        amount >= 10_000 -> "${formatter.format(amount / 10_000)}만"
        else -> formatter.format(amount)
    }
}
