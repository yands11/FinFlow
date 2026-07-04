package com.finflow.app.ui.composable.main.flow

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import java.text.DecimalFormat

private val chartColors = listOf(
    Color(0xFFE57373),
    Color(0xFF81C784),
    Color(0xFF64B5F6),
    Color(0xFFFFB74D),
    Color(0xFFBA68C8),
    Color(0xFF4DD0E1),
    Color(0xFFFF8A65),
    Color(0xFFA1887F),
    Color(0xFF90A4AE),
    Color(0xFFF06292),
)

data class CategorySlice(
    val category: ExpenseCategory,
    val amount: Long,
    val ratio: Float,
    val color: Color,
)

@Composable
fun CategoryDonutChart(
    allExpenses: List<Expense>,
    modifier: Modifier = Modifier,
) {
    val slices = remember(allExpenses) {
        val total = allExpenses.sumOf { it.amount }
        if (total == 0L) return@remember emptyList()

        allExpenses
            .groupBy { it.category }
            .entries
            .sortedByDescending { it.value.sumOf { e -> e.amount } }
            .mapIndexed { index, (category, items) ->
                val amount = items.sumOf { it.amount }
                CategorySlice(
                    category = category,
                    amount = amount,
                    ratio = amount.toFloat() / total,
                    color = chartColors[index % chartColors.size],
                )
            }
    }

    if (slices.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val totalAmount = slices.sumOf { it.amount }
    val centerTextColor = MaterialTheme.colorScheme.onSurface
    val centerLabelColor = MaterialTheme.colorScheme.onSurfaceVariant

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
                text = stringResource(R.string.category_expense_ratio_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 도넛 차트
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val strokeWidth = 40f
                val diameter = size.height - strokeWidth
                val topLeft = Offset(
                    (size.width - diameter) / 2f,
                    strokeWidth / 2f
                )
                val arcSize = Size(diameter, diameter)

                var startAngle = -90f
                slices.forEach { slice ->
                    val sweep = slice.ratio * 360f
                    drawArc(
                        color = slice.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                    )
                    startAngle += sweep
                }

                // 중앙 텍스트
                val centerX = size.width / 2f
                val centerY = size.height / 2f

                val labelResult = textMeasurer.measure(
                    text = "총 지출",
                    style = TextStyle(fontSize = 12.sp, color = centerLabelColor)
                )
                drawText(
                    textLayoutResult = labelResult,
                    topLeft = Offset(
                        centerX - labelResult.size.width / 2f,
                        centerY - labelResult.size.height - 2f
                    )
                )

                val amountResult = textMeasurer.measure(
                    text = "₩${DecimalFormat("#,###").format(totalAmount)}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = centerTextColor
                    )
                )
                drawText(
                    textLayoutResult = amountResult,
                    topLeft = Offset(
                        centerX - amountResult.size.width / 2f,
                        centerY + 2f
                    )
                )
            }

            // 범례
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                slices.forEach { slice ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(color = slice.color)
                        }
                        Text(
                            text = slice.category.name,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                        )
                        Text(
                            text = "${(slice.ratio * 100).toInt()}%",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "₩${DecimalFormat("#,###").format(slice.amount)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}
