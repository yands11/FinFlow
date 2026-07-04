package com.finflow.app.ui.composable.main.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.domain.model.flow.expense.BankAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManagementScreen(
    accounts: List<BankAccount>,
    onBack: () -> Unit,
    onAdd: (name: String, bankName: String, accountNumber: String, ownerName: String) -> Unit,
    onUpdate: (id: Long, name: String, bankName: String, accountNumber: String, ownerName: String) -> Unit,
    onDelete: (id: Long) -> Unit,
) {
    var editingAccount by rememberSaveable { mutableStateOf<BankAccount?>(null) }
    var showAddSheet by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.setting_account_management)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = { showAddSheet = true }) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.label_add))
                }
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(accounts, key = { it.id }) { account ->
                AccountRow(
                    account = account,
                    onClick = { editingAccount = account }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showAddSheet) {
        AccountFormSheet(
            title = stringResource(R.string.label_add_new_account),
            onDismiss = { showAddSheet = false },
            onSave = { name, bankName, accountNumber, ownerName ->
                onAdd(name, bankName, accountNumber, ownerName)
                showAddSheet = false
            }
        )
    }

    editingAccount?.let { account ->
        AccountFormSheet(
            title = stringResource(R.string.setting_account_management),
            initialName = account.name,
            initialBankName = account.bankName,
            initialAccountNumber = account.accountNumber,
            initialOwnerName = account.ownerName,
            onDismiss = { editingAccount = null },
            onSave = { name, bankName, accountNumber, ownerName ->
                onUpdate(account.id, name, bankName, accountNumber, ownerName)
                editingAccount = null
            },
            onDelete = {
                onDelete(account.id)
                editingAccount = null
            }
        )
    }
}

@Composable
private fun AccountRow(
    account: BankAccount,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${account.bankName} · ${account.accountNumber}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (account.ownerName.isNotBlank()) {
                Text(
                    text = account.ownerName,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountFormSheet(
    title: String,
    initialName: String = "",
    initialBankName: String = "",
    initialAccountNumber: String = "",
    initialOwnerName: String = "",
    onDismiss: () -> Unit,
    onSave: (name: String, bankName: String, accountNumber: String, ownerName: String) -> Unit,
    onDelete: (() -> Unit)? = null,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var bankName by rememberSaveable { mutableStateOf(initialBankName) }
    var accountNumber by rememberSaveable { mutableStateOf(initialAccountNumber) }
    var ownerName by rememberSaveable { mutableStateOf(initialOwnerName) }
    var showError by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; showError = false },
                label = { Text(stringResource(R.string.label_account_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it; showError = false },
                label = { Text(stringResource(R.string.label_bank_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text(stringResource(R.string.label_account_number)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = { Text(stringResource(R.string.label_owner_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

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
                if (onDelete != null) {
                    if (showDeleteConfirm) {
                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.label_confirm))
                        }
                    } else {
                        OutlinedButton(
                            onClick = { showDeleteConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.label_delete))
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.label_cancel))
                    }
                }
                Button(
                    onClick = {
                        if (name.isBlank() || bankName.isBlank()) {
                            showError = true
                        } else {
                            onSave(name.trim(), bankName.trim(), accountNumber.trim(), ownerName.trim())
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.label_save))
                }
            }
        }
    }
}
