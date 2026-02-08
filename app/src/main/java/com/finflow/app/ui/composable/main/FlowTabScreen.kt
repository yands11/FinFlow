package com.finflow.app.ui.composable.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.domain.model.flow.expense.ExpenseCategory

@Composable
fun FlowTabScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        IncomeCard(
            incomeAmount = 5_000_000L
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        ExpenseList(
            generalExpenseList = listOf(
                Expense.General(
                    id = 1,
                    category = ExpenseCategory(id = 1, name = "생활비"),
                    name = "장보기",
                    amount = 400_000L,
                    bankAccount = BankAccount(
                        id = 1,
                        name = "생활비",
                        bankName = "우리은행",
                        accountNumber = "123-456-7890",
                        ownerName = "John",
                        description = "",
                    )
                ),
                Expense.General(
                    id = 2,
                    category = ExpenseCategory(id = 1, name = "생활비"),
                    name = "외식비",
                    amount = 300_000L,
                    bankAccount = BankAccount(
                        id = 1,
                        name = "생활비",
                        bankName = "우리은행",
                        accountNumber = "123-456-7890",
                        ownerName = "John",
                        description = "",
                    )
                ),
                Expense.General(
                    id = 3,
                    category = ExpenseCategory(id = 2, name = "자동이체"),
                    name = "통신비",
                    amount = 70_000L,
                    bankAccount = BankAccount(
                        id = 2,
                        name = "자동이체",
                        bankName = "국민은행",
                        accountNumber = "999-456-7890",
                        ownerName = "Marry",
                        description = "",
                    )
                )
            ),
            savingExpenseList = listOf(
                Expense.Saving(
                    id = 1,
                    category = ExpenseCategory(id = 99, name = "청약"),
                    name = "청약저축",
                    amount = 300_000L,
                    bankAccount = BankAccount(
                        id = 99,
                        name = "청약",
                        bankName = "신한은행",
                        accountNumber = "4444-5552-1242-1",
                        ownerName = "John",
                        description = "",
                    )
                ),
            ),
            investmentExpenseList = listOf(
                Expense.Investment(
                    id = 1,
                    category = ExpenseCategory(id = 99, name = "주식"),
                    name = "ETF",
                    amount = 500_000L,
                    bankAccount = BankAccount(
                        id = 100,
                        name = "주식계좌",
                        bankName = "카카오페이증권",
                        accountNumber = "4444-5552-1242-1",
                        ownerName = "John",
                        description = "",
                    )
                )
            ),
        )
    }
}