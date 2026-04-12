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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    val availableMonths by viewModel.availableMonths.collectAsState()

    val incomeList = monthFlow.incomeList
    val generalExpenseList = monthFlow.expenseList
    val savingExpenseList = monthFlow.savingList
    val investmentExpenseList = monthFlow.investmentList

    val monthLabel = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월"

    val hasPrevious = availableMonths.any { it < currentYearMonth }
    val hasNext = availableMonths.any { it > currentYearMonth }

    // 추가 모드 상태
    var showTypeSelect by rememberSaveable { mutableStateOf(false) }
    var showAddIncome by rememberSaveable { mutableStateOf(false) }
    var selectedExpenseType by rememberSaveable { mutableStateOf<AddItemType?>(null) }

    // 수정 모드 상태
    var editingIncome by rememberSaveable { mutableStateOf<CashIncome?>(null) }
    var editingExpense by rememberSaveable { mutableStateOf<EditingExpense?>(null) }

    // 다음 달 생성 다이얼로그
    var showNewMonthDialog by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MonthHeader(
                currentMonth = monthLabel,
                onPreviousMonth = viewModel::moveToPreviousMonth,
                onNextMonth = {
                    if (hasNext) {
                        viewModel.moveToNextMonth()
                    } else {
                        showNewMonthDialog = true
                    }
                },
                previousButtonEnabled = hasPrevious,
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
                                GroupedExpenseContent(
                                    expenses = generalExpenseList,
                                    itemType = AddItemType.GENERAL,
                                    onExpenseClick = { editingExpense = it }
                                )
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
                                GroupedExpenseContent(
                                    expenses = savingExpenseList,
                                    itemType = AddItemType.SAVING,
                                    onExpenseClick = { editingExpense = it }
                                )
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
                                GroupedExpenseContent(
                                    expenses = investmentExpenseList,
                                    itemType = AddItemType.INVESTMENT,
                                    onExpenseClick = { editingExpense = it }
                                )
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

                val allExpenses: List<Expense> = generalExpenseList + savingExpenseList + investmentExpenseList
                if (allExpenses.isNotEmpty()) {
                    item {
                        CategoryDonutChart(allExpenses = allExpenses)
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

    // === 다음 달 생성 다이얼로그 ===

    if (showNewMonthDialog) {
        val nextMonth = currentYearMonth.plusMonths(1)
        val nextLabel = "${nextMonth.year}년 ${nextMonth.monthValue}월"

        AlertDialog(
            onDismissRequest = { showNewMonthDialog = false },
            title = { Text(stringResource(R.string.new_month_dialog_title)) },
            text = { Text(stringResource(R.string.new_month_dialog_message, nextLabel)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.copyPreviousMonth()
                    showNewMonthDialog = false
                }) {
                    Text(stringResource(R.string.new_month_copy_previous))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.createEmptyMonth()
                    showNewMonthDialog = false
                }) {
                    Text(stringResource(R.string.new_month_create_new))
                }
            }
        )
    }
}

@Composable
private fun GroupedExpenseContent(
    expenses: List<Expense>,
    itemType: AddItemType,
    onExpenseClick: (EditingExpense) -> Unit,
) {
    val grouped = expenses.groupBy { it.category }
    grouped.forEach { (category, items) ->
        // 카테고리 헤더
        ExpenseCategoryTag(
            category = category,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        // 해당 카테고리의 아이템들
        items.forEach { expense ->
            ExpenseItem(
                name = expense.name,
                amount = expense.amount,
                bankAccountName = expense.bankAccount.name,
                onClick = {
                    onExpenseClick(
                        EditingExpense(
                            id = expense.id,
                            type = itemType,
                            name = expense.name,
                            amount = expense.amount,
                            categoryId = expense.category.id,
                            bankAccountId = expense.bankAccount.id,
                        )
                    )
                }
            )
        }
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
