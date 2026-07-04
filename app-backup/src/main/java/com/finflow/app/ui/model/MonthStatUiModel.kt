package com.finflow.app.ui.model

data class MonthStatUiModel(
    val monthLabel: String,
    val incomeTotal: Long,
    val expenseTotal: Long,
    val savingTotal: Long,
    val investmentTotal: Long,
) {
    val outflowTotal: Long get() = expenseTotal + savingTotal + investmentTotal

    val expenseRatio: Float
        get() = if (outflowTotal == 0L) 0f else expenseTotal.toFloat() / outflowTotal

    val savingRatio: Float
        get() = if (outflowTotal == 0L) 0f else savingTotal.toFloat() / outflowTotal

    val investmentRatio: Float
        get() = if (outflowTotal == 0L) 0f else investmentTotal.toFloat() / outflowTotal
}
