package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.CashIncome
import com.finflow.app.domain.model.flow.expense.Expense
import java.text.DecimalFormat

@Composable
fun SummarySection(
    incomeList: List<CashIncome>,
    expenseList: List<Expense.General>,
    savingList: List<Expense.Saving>,
    investmentList: List<Expense.Investment>,
    modifier: Modifier = Modifier
) {
    val incomeTotal = incomeList.sumOf { it.amount }
    val expenseTotal = expenseList.sumOf { it.amount }
    val savingTotal = savingList.sumOf { it.amount }
    val investmentTotal = investmentList.sumOf { it.amount }

    val remaining = incomeTotal - expenseTotal - savingTotal - investmentTotal

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
            SummaryRow(
                label = stringResource(id = R.string.income_title),
                amount = incomeTotal
            )
            SummaryRow(
                label = stringResource(id = R.string.general_expense_title),
                amount = expenseTotal
            )
            SummaryRow(
                label = stringResource(id = R.string.saving_expense_title),
                amount = savingTotal
            )
            SummaryRow(
                label = stringResource(id = R.string.investment_expense_title),
                amount = investmentTotal
            )
            if (remaining != 0L) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SummaryRow(
                    label = stringResource(id = R.string.remaining_title),
                    amount = remaining,
                    amountColor = if (remaining > 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: Long,
    amountColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "₩ ${DecimalFormat("#,###").format(amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}
