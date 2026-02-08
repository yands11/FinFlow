package com.finflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.finflow.app.ui.composable.main.FlowTabScreen
import com.finflow.app.ui.composable.main.MainNavigationBar
import com.finflow.app.ui.composable.main.dashboard.DashboardTabScreen
import com.finflow.app.ui.composable.main.setting.SettingTabScreen
import com.finflow.app.ui.model.MainNavTab
import com.finflow.app.ui.theme.FinFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinFlowTheme {
                var selectedTabId by rememberSaveable { mutableIntStateOf(MainNavTab.Flow.id) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MainNavigationBar(
                            selectedTabId = selectedTabId,
                            onTabSelected = { selectedTabId = it.id }
                        )
                    },
                ) { innerPadding ->
                    when (selectedTabId) {
                        MainNavTab.Flow.id ->
                            FlowTabScreen(
                                modifier = Modifier.padding(innerPadding)
                            )

                        MainNavTab.Dashboard.id ->
                            DashboardTabScreen(
                                modifier = Modifier.padding(innerPadding)
                            )

                        MainNavTab.Setting.id ->
                            SettingTabScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                    }
                }
            }
        }
    }
}
