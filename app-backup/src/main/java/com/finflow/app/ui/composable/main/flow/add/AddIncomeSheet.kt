package com.finflow.app.ui.composable.main.flow.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finflow.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, amount: Long) -> Unit,
    editId: Long? = null,
    initialTitle: String = "",
    initialAmount: String = "",
    onDelete: ((Long) -> Unit)? = null,
) {
    val isEditMode = editId != null
    var title by rememberSaveable { mutableStateOf(initialTitle) }
    var amountText by rememberSaveable { mutableStateOf(initialAmount) }
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
                text = stringResource(
                    if (isEditMode) R.string.edit_income_title else R.string.add_income_title
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it; showError = false },
                label = { Text(stringResource(R.string.label_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                        if (title.isBlank() || amount == null || amount <= 0) {
                            showError = true
                        } else {
                            onSave(title.trim(), amount)
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
