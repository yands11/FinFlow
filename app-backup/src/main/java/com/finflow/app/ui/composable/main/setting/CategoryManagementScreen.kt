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
import com.finflow.app.domain.model.flow.expense.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    categories: List<ExpenseCategory>,
    onBack: () -> Unit,
    onAdd: (name: String) -> Unit,
    onUpdate: (id: Long, name: String) -> Unit,
    onDelete: (id: Long) -> Unit,
) {
    var editingCategory by rememberSaveable { mutableStateOf<ExpenseCategory?>(null) }
    var showAddSheet by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.setting_category_management)) },
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
            items(categories, key = { it.id }) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { editingCategory = category }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showAddSheet) {
        CategoryFormSheet(
            title = stringResource(R.string.label_add_new_category),
            onDismiss = { showAddSheet = false },
            onSave = { name ->
                onAdd(name)
                showAddSheet = false
            }
        )
    }

    editingCategory?.let { category ->
        CategoryFormSheet(
            title = stringResource(R.string.setting_category_management),
            initialName = category.name,
            onDismiss = { editingCategory = null },
            onSave = { name ->
                onUpdate(category.id, name)
                editingCategory = null
            },
            onDelete = {
                onDelete(category.id)
                editingCategory = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFormSheet(
    title: String,
    initialName: String = "",
    onDismiss: () -> Unit,
    onSave: (name: String) -> Unit,
    onDelete: (() -> Unit)? = null,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
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
                label = { Text(stringResource(R.string.label_category_name)) },
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
                        if (name.isBlank()) {
                            showError = true
                        } else {
                            onSave(name.trim())
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
