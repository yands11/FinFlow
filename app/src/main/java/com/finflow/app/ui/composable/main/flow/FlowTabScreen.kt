package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflow.app.R
import com.finflow.app.domain.model.flow.CashIncome
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.ui.composable.main.flow.add.AddExpenseSheet
import com.finflow.app.ui.composable.main.flow.add.AddIncomeSheet
import com.finflow.app.ui.composable.main.flow.add.AddItemType
import com.finflow.app.ui.composable.main.flow.add.AddTypeSelectSheet
import com.finflow.app.ui.viewmodel.FlowViewModel

@Composable
fun FlowTabScreen(
    modifier: Modifier = Modifier,
    viewModel: FlowViewModel = hiltViewModel(),
) {
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()
    val monthFlow by viewModel.monthFlow.collectAsState()
    val bankAccounts by viewModel.bankAccounts.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val incomeList = monthFlow.incomeList
    val generalExpenseList = monthFlow.expenseList
    val savingExpenseList = monthFlow.savingList
    val investmentExpenseList = monthFlow.investmentList

    val monthLabel = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월"

    // 추가 모드 상태
    var showTypeSelect by rememberSaveable { mutableStateOf(false) }
    var showAddIncome by rememberSaveable { mutableStateOf(false) }
    var selectedExpenseType by rememberSaveable { mutableStateOf<AddItemType?>(null) }

    // 수정 모드 상태
    var editingIncome by rememberSaveable { mutableStateOf<CashIncome?>(null) }
    var editingExpense by rememberSaveable { mutableStateOf<EditingExpense?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    IncomeSectionCard(
                        incomeList = incomeList,
                        onIncomeClick = { income -> editingIncome = income }
                    )
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
                                        bankAccountName = expense.bankAccount.name,
                                        onClick = {
                                            editingExpense = EditingExpense(
                                                id = expense.id,
                                                type = AddItemType.GENERAL,
                                                name = expense.name,
                                                amount = expense.amount,
                                                categoryId = expense.category.id,
                                                bankAccountId = expense.bankAccount.id,
                                            )
                                        }
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
                                        bankAccountName = expense.bankAccount.name,
                                        onClick = {
                                            editingExpense = EditingExpense(
                                                id = expense.id,
                                                type = AddItemType.SAVING,
                                                name = expense.name,
                                                amount = expense.amount,
                                                categoryId = expense.category.id,
                                                bankAccountId = expense.bankAccount.id,
                                            )
                                        }
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
                                        bankAccountName = expense.bankAccount.name,
                                        onClick = {
                                            editingExpense = EditingExpense(
                                                id = expense.id,
                                                type = AddItemType.INVESTMENT,
                                                name = expense.name,
                                                amount = expense.amount,
                                                categoryId = expense.category.id,
                                                bankAccountId = expense.bankAccount.id,
                                            )
                                        }
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

        // FAB
        FloatingActionButton(
            onClick = { showTypeSelect = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_type_select_title),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    // === 추가 모드 시트 ===

    if (showTypeSelect) {
        AddTypeSelectSheet(
            onDismiss = { showTypeSelect = false },
            onTypeSelected = { type ->
                showTypeSelect = false
                when (type) {
                    AddItemType.INCOME -> showAddIncome = true
                    else -> selectedExpenseType = type
                }
            }
        )
    }

    if (showAddIncome) {
        AddIncomeSheet(
            onDismiss = { showAddIncome = false },
            onSave = { title, amount ->
                viewModel.addIncome(title, amount)
                showAddIncome = false
            }
        )
    }

    selectedExpenseType?.let { type ->
        val sheetTitle = when (type) {
            AddItemType.GENERAL -> stringResource(R.string.add_expense_title)
            AddItemType.SAVING -> stringResource(R.string.add_saving_title)
            AddItemType.INVESTMENT -> stringResource(R.string.add_investment_title)
            else -> ""
        }
        AddExpenseSheet(
            title = sheetTitle,
            expenseType = type,
            categories = categories,
            bankAccounts = bankAccounts,
            onDismiss = { selectedExpenseType = null },
            onSave = { expenseType, categoryId, name, amount, bankAccountId ->
                viewModel.addExpense(expenseType, categoryId, name, amount, bankAccountId)
                selectedExpenseType = null
            },
            onAddCategory = { name, onCreated -> viewModel.addCategory(name, onCreated) },
            onAddBankAccount = { name, bankName, accountNumber, ownerName, onCreated ->
                viewModel.addBankAccount(name, bankName, accountNumber, ownerName, onCreated)
            }
        )
    }

    // === 수정 모드 시트 ===

    editingIncome?.let { income ->
        AddIncomeSheet(
            onDismiss = { editingIncome = null },
            onSave = { title, amount ->
                viewModel.updateIncome(income.id, title, amount)
                editingIncome = null
            },
            editId = income.id,
            initialTitle = income.title,
            initialAmount = income.amount.toString(),
            onDelete = { id ->
                viewModel.deleteIncome(id)
                editingIncome = null
            }
        )
    }

    editingExpense?.let { expense ->
        val sheetTitle = when (expense.type) {
            AddItemType.GENERAL -> stringResource(R.string.edit_expense_title)
            AddItemType.SAVING -> stringResource(R.string.edit_saving_title)
            AddItemType.INVESTMENT -> stringResource(R.string.edit_investment_title)
            else -> ""
        }
        AddExpenseSheet(
            title = sheetTitle,
            expenseType = expense.type,
            categories = categories,
            bankAccounts = bankAccounts,
            onDismiss = { editingExpense = null },
            onSave = { _, categoryId, name, amount, bankAccountId ->
                viewModel.updateExpense(expense.id, categoryId, name, amount, bankAccountId)
                editingExpense = null
            },
            onAddCategory = { name, onCreated -> viewModel.addCategory(name, onCreated) },
            onAddBankAccount = { name, bankName, accountNumber, ownerName, onCreated ->
                viewModel.addBankAccount(name, bankName, accountNumber, ownerName, onCreated)
            },
            editId = expense.id,
            initialName = expense.name,
            initialAmount = expense.amount.toString(),
            initialCategoryId = expense.categoryId,
            initialAccountId = expense.bankAccountId,
            onDelete = { id ->
                viewModel.deleteExpense(id)
                editingExpense = null
            }
        )
    }
}

private data class EditingExpense(
    val id: Long,
    val type: AddItemType,
    val name: String,
    val amount: Long,
    val categoryId: Long,
    val bankAccountId: Long,
)
