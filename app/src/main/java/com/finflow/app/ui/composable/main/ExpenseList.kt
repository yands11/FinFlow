package com.finflow.app.ui.composable.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.finflow.app.R
import com.finflow.app.domain.model.flow.expense.Expense

@Composable
fun ExpenseList(
    generalExpenseList: List<Expense.General>,
    savingExpenseList: List<Expense.Saving>,
    investmentExpenseList: List<Expense.Investment>
) {

    if (generalExpenseList.isNotEmpty()) {
        ExpenseSectionCard(
            sectionTitle = stringResource(R.string.general_expense_title),
            totalAmount = generalExpenseList.sumOf { it.amount },
            content = {
                LazyColumn {
                    items(generalExpenseList) { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            }
        )
    }

    if (savingExpenseList.isNotEmpty()) {
        ExpenseSectionCard(
            sectionTitle = stringResource(R.string.saving_expense_title),
            totalAmount = savingExpenseList.sumOf { it.amount },
            content = {
                LazyColumn {
                    items(savingExpenseList) { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            }
        )
    }

    if (investmentExpenseList.isNotEmpty()) {
        ExpenseSectionCard(
            sectionTitle = stringResource(R.string.investment_expense_title),
            totalAmount = investmentExpenseList.sumOf { it.amount },
            content = {
                LazyColumn {
                    items(investmentExpenseList) { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            }
        )
    }
}
