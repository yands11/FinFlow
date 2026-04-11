package com.finflow.app.ui.composable.main.flow.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finflow.app.R

enum class AddItemType {
    INCOME, GENERAL, SAVING, INVESTMENT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTypeSelectSheet(
    onDismiss: () -> Unit,
    onTypeSelected: (AddItemType) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.add_type_select_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            TypeRow(
                icon = Icons.Filled.AttachMoney,
                label = stringResource(R.string.income_title),
                onClick = { onTypeSelected(AddItemType.INCOME) }
            )
            TypeRow(
                icon = Icons.Filled.AccountBalanceWallet,
                label = stringResource(R.string.general_expense_title),
                onClick = { onTypeSelected(AddItemType.GENERAL) }
            )
            TypeRow(
                icon = Icons.Filled.Savings,
                label = stringResource(R.string.saving_expense_title),
                onClick = { onTypeSelected(AddItemType.SAVING) }
            )
            TypeRow(
                icon = Icons.Filled.TrendingUp,
                label = stringResource(R.string.investment_expense_title),
                onClick = { onTypeSelected(AddItemType.INVESTMENT) }
            )
        }
    }
}

@Composable
private fun TypeRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
