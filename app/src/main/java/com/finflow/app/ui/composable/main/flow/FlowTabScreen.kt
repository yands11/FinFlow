package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.CashIncome
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.domain.model.flow.expense.ExpenseCategory

@Composable
fun FlowTabScreen(
    modifier: Modifier = Modifier
) {
    val incomeList = listOf(
        CashIncome(id = 1, title = "월급", amount = 5_440_000L),
        CashIncome(id = 2, title = "인센티브", amount = 1_500_000L)
    )
    val generalExpenseList = listOf(
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
    )
    val savingExpenseList = listOf(
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
    )
    val investmentExpenseList = listOf(
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
    )
    val bankAccounts = listOf(
        BankAccount(
            id = 1,
            name = "생활비",
            bankName = "우리은행",
            accountNumber = "123-456-7890",
            ownerName = "John",
            description = ""
        ),
        BankAccount(
            id = 2,
            name = "자동이체",
            bankName = "국민은행",
            accountNumber = "999-456-7890",
            ownerName = "Marry",
            description = ""
        ),
        BankAccount(
            id = 99,
            name = "청약",
            bankName = "신한은행",
            accountNumber = "4444-5552-1242-1",
            ownerName = "John",
            description = ""
        ),
        BankAccount(
            id = 100,
            name = "주식계좌",
            bankName = "카카오페이증권",
            accountNumber = "4444-5552-1242-1",
            ownerName = "John",
            description = ""
        )
    )

    Column(modifier = modifier.fillMaxSize()) {
        MonthHeader(
            currentMonth = "2024년 4월",
            onPreviousMonth = {},
            onNextMonth = {},
            previousButtonEnabled = true,
            nextButtonEnabled = true
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummarySection(
                    incomeList = incomeList,
                    expenseList = generalExpenseList,
                    savingList = savingExpenseList,
                    investmentList = investmentExpenseList
                )
            }

            item {
                IncomeSectionCard(incomeList = incomeList)
            }

            if (generalExpenseList.isNotEmpty()) {
                item {
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
            }

            if (savingExpenseList.isNotEmpty()) {
                item {
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
            }

            if (investmentExpenseList.isNotEmpty()) {
                item {
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

            item {
                AccountSummaryCard(
                    bankAccounts = bankAccounts,
                    expenseList = generalExpenseList + savingExpenseList + investmentExpenseList
                )
            }
        }
    }
}
