package com.finflow.app.ui.composable.main.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.finflow.app.ui.model.MonthStatUiModel

@Composable
fun DashboardTabScreen(
    modifier: Modifier = Modifier
) {
    val monthStats = sampleMonthStats()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            IncomeLineChart(monthStats = monthStats)
        }

        item {
            ExpenseRatioBarChart(monthStats = monthStats)
        }
    }
}

private fun sampleMonthStats(): List<MonthStatUiModel> = listOf(
    MonthStatUiModel(
        monthLabel = "11월",
        incomeTotal = 5_440_000,
        expenseTotal = 1_200_000,
        savingTotal = 300_000,
        investmentTotal = 400_000
    ),
    MonthStatUiModel(
        monthLabel = "12월",
        incomeTotal = 5_440_000,
        expenseTotal = 1_500_000,
        savingTotal = 300_000,
        investmentTotal = 500_000
    ),
    MonthStatUiModel(
        monthLabel = "1월",
        incomeTotal = 5_440_000,
        expenseTotal = 900_000,
        savingTotal = 300_000,
        investmentTotal = 500_000
    ),
    MonthStatUiModel(
        monthLabel = "2월",
        incomeTotal = 5_440_000,
        expenseTotal = 1_100_000,
        savingTotal = 300_000,
        investmentTotal = 500_000
    ),
    MonthStatUiModel(
        monthLabel = "3월",
        incomeTotal = 6_940_000,
        expenseTotal = 770_000,
        savingTotal = 300_000,
        investmentTotal = 500_000
    ),
    MonthStatUiModel(
        monthLabel = "4월",
        incomeTotal = 6_940_000,
        expenseTotal = 770_000,
        savingTotal = 300_000,
        investmentTotal = 500_000
    ),
)
