package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.expense.Expense

@Composable
fun ExpenseList(
    generalExpenseList: List<Expense.General>,
    savingExpenseList: List<Expense.Saving>,
    investmentExpenseList: List<Expense.Investment>
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (generalExpenseList.isNotEmpty()) {
            ExpenseSectionCard(
                sectionTitle = stringResource(R.string.general_expense_title),
                totalAmount = generalExpenseList.sumOf { it.amount },
                content = {
                    generalExpenseList.forEach { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            )
        }

        if (savingExpenseList.isNotEmpty()) {
            ExpenseSectionCard(
                sectionTitle = stringResource(R.string.saving_expense_title),
                totalAmount = savingExpenseList.sumOf { it.amount },
                content = {
                    savingExpenseList.forEach { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            )
        }

        if (investmentExpenseList.isNotEmpty()) {
            ExpenseSectionCard(
                sectionTitle = stringResource(R.string.investment_expense_title),
                totalAmount = investmentExpenseList.sumOf { it.amount },
                content = {
                    investmentExpenseList.forEach { expense ->
                        ExpenseItem(
                            category = expense.category,
                            name = expense.name,
                            amount = expense.amount,
                            bankAccountName = expense.bankAccount.name
                        )
                    }
                }
            )
        }
    }
}
