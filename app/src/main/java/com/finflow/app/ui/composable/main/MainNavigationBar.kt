package com.finflow.app.ui.composable.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.finflow.app.ui.model.MainNavTab

@Composable
fun MainNavigationBar(
    selectedTabId: Int,
    onTabSelected: (MainNavTab) -> Unit,
) {
    NavigationBar {
        listOf(
            MainNavTab.Flow,
            MainNavTab.Dashboard,
            MainNavTab.Setting,
        ).forEach {
            NavigationBarItem(
                selected = selectedTabId == it.id,
                onClick = { onTabSelected(it) },
                icon = {
                    Icon(
                        painter = painterResource(it.iconResId),
                        contentDescription = stringResource(it.nameResId)
                    )
                },
                label = { Text(stringResource(it.nameResId)) }
            )
        }
    }
}
