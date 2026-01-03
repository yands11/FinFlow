package com.finflow.app.domain.model.flow

import com.finflow.app.domain.model.flow.expense.Expense


data class MonthFlow(
    val incomeList: List<CashIncome> = emptyList(),
    val expenseList: List<Expense.General> = emptyList(),
    val savingList: List<Expense.Saving> = emptyList(),
    val investmentList: List<Expense.Investment> = emptyList(),
)