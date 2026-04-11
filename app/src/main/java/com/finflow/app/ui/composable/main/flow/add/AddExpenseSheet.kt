package com.finflow.app.ui.composable.main.flow.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseSheet(
    title: String,
    expenseType: AddItemType,
    categories: List<ExpenseCategory>,
    bankAccounts: List<BankAccount>,
    onDismiss: () -> Unit,
    onSave: (type: String, categoryId: Long, name: String, amount: Long, bankAccountId: Long) -> Unit,
    onAddCategory: (name: String, onCreated: (Long) -> Unit) -> Unit,
    onAddBankAccount: (name: String, bankName: String, accountNumber: String, ownerName: String, onCreated: (Long) -> Unit) -> Unit,
    editId: Long? = null,
    initialName: String = "",
    initialAmount: String = "",
    initialCategoryId: Long = -1L,
    initialAccountId: Long = -1L,
    onDelete: ((Long) -> Unit)? = null,
) {
    val isEditMode = editId != null
    var name by rememberSaveable { mutableStateOf(initialName) }
    var amountText by rememberSaveable { mutableStateOf(initialAmount) }
    var selectedCategoryId by rememberSaveable { mutableLongStateOf(initialCategoryId) }
    var selectedAccountId by rememberSaveable { mutableLongStateOf(initialAccountId) }
    var showError by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    // 인라인 카테고리 추가
    var showNewCategory by rememberSaveable { mutableStateOf(false) }
    var newCategoryName by rememberSaveable { mutableStateOf("") }

    // 인라인 계좌 추가
    var showNewAccount by rememberSaveable { mutableStateOf(false) }
    var newAccountName by rememberSaveable { mutableStateOf("") }
    var newBankName by rememberSaveable { mutableStateOf("") }
    var newAccountNumber by rememberSaveable { mutableStateOf("") }
    var newOwnerName by rememberSaveable { mutableStateOf("") }

    val typeString = when (expenseType) {
        AddItemType.GENERAL -> "GENERAL"
        AddItemType.SAVING -> "SAVING"
        AddItemType.INVESTMENT -> "INVESTMENT"
        else -> "GENERAL"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 이름
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; showError = false },
                label = { Text(stringResource(R.string.label_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 금액
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it.filter { c -> c.isDigit() }; showError = false },
                label = { Text(stringResource(R.string.label_amount)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = AmountVisualTransformation(),
                suffix = { Text("원") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 카테고리 선택
            CategoryDropdown(
                categories = categories,
                selectedId = selectedCategoryId,
                onSelected = { selectedCategoryId = it; showError = false }
            )

            // 새 카테고리 추가
            if (!showNewCategory) {
                TextButton(onClick = { showNewCategory = true }) {
                    Text(stringResource(R.string.label_add_new_category))
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text(stringResource(R.string.label_category_name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                onAddCategory(newCategoryName.trim()) { id ->
                                    selectedCategoryId = id
                                    newCategoryName = ""
                                    showNewCategory = false
                                }
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    ) {
                        Text(stringResource(R.string.label_add))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 계좌 선택
            AccountDropdown(
                accounts = bankAccounts,
                selectedId = selectedAccountId,
                onSelected = { selectedAccountId = it; showError = false }
            )

            // 새 계좌 추가
            if (!showNewAccount) {
                TextButton(onClick = { showNewAccount = true }) {
                    Text(stringResource(R.string.label_add_new_account))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    OutlinedTextField(
                        value = newAccountName,
                        onValueChange = { newAccountName = it },
                        label = { Text(stringResource(R.string.label_account_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newBankName,
                        onValueChange = { newBankName = it },
                        label = { Text(stringResource(R.string.label_bank_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newAccountNumber,
                        onValueChange = { newAccountNumber = it },
                        label = { Text(stringResource(R.string.label_account_number)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newOwnerName,
                        onValueChange = { newOwnerName = it },
                        label = { Text(stringResource(R.string.label_owner_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newAccountName.isNotBlank() && newBankName.isNotBlank()) {
                                onAddBankAccount(
                                    newAccountName.trim(),
                                    newBankName.trim(),
                                    newAccountNumber.trim(),
                                    newOwnerName.trim(),
                                ) { id ->
                                    selectedAccountId = id
                                    newAccountName = ""
                                    newBankName = ""
                                    newAccountNumber = ""
                                    newOwnerName = ""
                                    showNewAccount = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.label_add))
                    }
                }
            }

            if (showError) {
                Text(
                    text = stringResource(R.string.error_empty_field),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                if (isEditMode && onDelete != null) {
                    if (showDeleteConfirm) {
                        Button(
                            onClick = {
                                onDelete(editId!!)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.label_confirm))
                        }
                    } else {
                        OutlinedButton(
                            onClick = { showDeleteConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.label_delete))
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.label_cancel))
                    }
                }
                Button(
                    onClick = {
                        val amount = amountText.toLongOrNull()
                        if (name.isBlank() || amount == null || amount <= 0
                            || selectedCategoryId < 0 || selectedAccountId < 0
                        ) {
                            showError = true
                        } else {
                            onSave(typeString, selectedCategoryId, name.trim(), amount, selectedAccountId)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.label_save))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    categories: List<ExpenseCategory>,
    selectedId: Long,
    onSelected: (Long) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selectedName = categories.firstOrNull { it.id == selectedId }?.name ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_category)) },
            placeholder = { Text(stringResource(R.string.hint_select_category)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onSelected(category.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDropdown(
    accounts: List<BankAccount>,
    selectedId: Long,
    onSelected: (Long) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.id == selectedId }
    val displayText = selected?.let { "${it.name} (${it.bankName})" } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_bank_account)) },
            placeholder = { Text(stringResource(R.string.hint_select_account)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            accounts.forEach { account ->
                DropdownMenuItem(
                    text = { Text("${account.name} (${account.bankName})") },
                    onClick = {
                        onSelected(account.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
