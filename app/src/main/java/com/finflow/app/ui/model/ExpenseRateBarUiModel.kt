package com.finflow.app.ui.model

import com.finflow.app.domain.model.flow.expense.Expense

data class ExpenseRateBarUiModel(
    val cashRatio: Float,
    val savingRatio: Float,
    val investmentRatio: Float,
) {
    companion object {
        fun from(
            generalExpense: Expense.General,
            savingExpense: Expense.Saving,
            investmentExpense: Expense.Investment,
        ): ExpenseRateBarUiModel {
            val sum = generalExpense.amount + savingExpense.amount + investmentExpense.amount
            return ExpenseRateBarUiModel(
                cashRatio = generalExpense.amount.toFloat() / sum,
                savingRatio = savingExpense.amount.toFloat() / sum,
                investmentRatio = investmentExpense.amount.toFloat() / sum,
            )
        }
    }
}