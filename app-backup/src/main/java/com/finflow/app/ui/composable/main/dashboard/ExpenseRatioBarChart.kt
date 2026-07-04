package com.finflow.app.ui.composable.main.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.ui.model.MonthStatUiModel

private val ExpenseColor = Color(0xFFE57373)
private val SavingColor = Color(0xFF81C784)
private val InvestmentColor = Color(0xFF64B5F6)

@Composable
fun ExpenseRatioBarChart(
    monthStats: List<MonthStatUiModel>,
    modifier: Modifier = Modifier
) {
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
                text = stringResource(R.string.dashboard_expense_ratio_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegendItem(color = ExpenseColor, label = stringResource(R.string.general_expense_title))
                LegendItem(color = SavingColor, label = stringResource(R.string.saving_expense_title))
                LegendItem(color = InvestmentColor, label = stringResource(R.string.investment_expense_title))
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                if (monthStats.isEmpty()) return@Canvas

                val leftPadding = 48f
                val rightPadding = 24f
                val topPadding = 8f
                val bottomPadding = 40f

                val chartWidth = size.width - leftPadding - rightPadding
                val chartHeight = size.height - topPadding - bottomPadding

                val barWidth = (chartWidth / monthStats.size) * 0.6f
                val barSpacing = chartWidth / monthStats.size

                // Y axis percentage labels
                val percentSteps = 4
                for (i in 0..percentSteps) {
                    val y = topPadding + chartHeight * (1f - i.toFloat() / percentSteps)
                    drawLine(
                        color = gridColor,
                        start = Offset(leftPadding, y),
                        end = Offset(size.width - rightPadding, y),
                        strokeWidth = 1f
                    )
                    val percent = "${i * 100 / percentSteps}%"
                    val textResult = textMeasurer.measure(
                        text = percent,
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

                // Draw stacked bars
                monthStats.forEachIndexed { index, stat ->
                    val centerX = leftPadding + barSpacing * index + barSpacing / 2f
                    val barLeft = centerX - barWidth / 2f

                    if (stat.outflowTotal > 0) {
                        // Expense (bottom)
                        val expenseHeight = chartHeight * stat.expenseRatio
                        drawRoundRect(
                            color = ExpenseColor,
                            topLeft = Offset(barLeft, topPadding + chartHeight - expenseHeight),
                            size = Size(barWidth, expenseHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )

                        // Saving (middle)
                        val savingHeight = chartHeight * stat.savingRatio
                        drawRoundRect(
                            color = SavingColor,
                            topLeft = Offset(
                                barLeft,
                                topPadding + chartHeight - expenseHeight - savingHeight
                            ),
                            size = Size(barWidth, savingHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )

                        // Investment (top)
                        val investmentHeight = chartHeight * stat.investmentRatio
                        drawRoundRect(
                            color = InvestmentColor,
                            topLeft = Offset(
                                barLeft,
                                topPadding + chartHeight - expenseHeight - savingHeight - investmentHeight
                            ),
                            size = Size(barWidth, investmentHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }

                    // X axis month label
                    val textResult = textMeasurer.measure(
                        text = stat.monthLabel,
                        style = TextStyle(fontSize = 10.sp, color = labelColor)
                    )
                    drawText(
                        textLayoutResult = textResult,
                        topLeft = Offset(
                            centerX - textResult.size.width / 2f,
                            size.height - textResult.size.height - 4f
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = color
        )
    }
}
