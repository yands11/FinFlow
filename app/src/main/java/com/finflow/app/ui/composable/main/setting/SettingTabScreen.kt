package com.finflow.app.ui.composable.main.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.finflow.app.R

@Composable
fun SettingTabScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SettingMenuItem(
            icon = Icons.Filled.AccountBalance,
            title = stringResource(R.string.setting_account_management),
            description = stringResource(R.string.setting_account_management_desc),
            onClick = { /* TODO */ }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingMenuItem(
            icon = Icons.AutoMirrored.Filled.List,
            title = stringResource(R.string.setting_category_management),
            description = stringResource(R.string.setting_category_management_desc),
            onClick = { /* TODO */ }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingMenuItem(
            icon = Icons.Filled.Info,
            title = stringResource(R.string.setting_app_info),
            description = stringResource(R.string.setting_app_version, "1.0.0"),
            onClick = { /* TODO */ }
        )
    }
}

@Composable
private fun SettingMenuItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier
                .size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
