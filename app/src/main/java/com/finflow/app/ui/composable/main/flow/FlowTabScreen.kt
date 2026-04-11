package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflow.app.R
import com.finflow.app.ui.viewmodel.FlowViewModel

@Composable
fun FlowTabScreen(
    modifier: Modifier = Modifier,
    viewModel: FlowViewModel = hiltViewModel(),
) {
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()
    val monthFlow by viewModel.monthFlow.collectAsState()
    val bankAccounts by viewModel.bankAccounts.collectAsState()

    val incomeList = monthFlow.incomeList
    val generalExpenseList = monthFlow.expenseList
    val savingExpenseList = monthFlow.savingList
    val investmentExpenseList = monthFlow.investmentList

    val monthLabel = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월"

    Column(modifier = modifier.fillMaxSize()) {
        MonthHeader(
            currentMonth = monthLabel,
            onPreviousMonth = viewModel::moveToPreviousMonth,
            onNextMonth = viewModel::moveToNextMonth,
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

            if (bankAccounts.isNotEmpty()) {
                item {
                    AccountSummaryCard(
                        bankAccounts = bankAccounts,
                        expenseList = generalExpenseList + savingExpenseList + investmentExpenseList
                    )
                }
            }
        }
    }
}
