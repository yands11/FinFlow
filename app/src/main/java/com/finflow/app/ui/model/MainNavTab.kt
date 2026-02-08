package com.finflow.app.ui.model

import com.finflow.app.R

sealed class MainNavTab(
    val id: Int,
    val nameResId: Int,
    val iconResId: Int,
) {
    object Flow : MainNavTab(
        id = 0,
        nameResId = R.string.tab_flow,
        iconResId = R.drawable.icon_tab_flow,
    )

    object Dashboard : MainNavTab(
        id = 1,
        nameResId = R.string.tab_dashboard,
        iconResId = R.drawable.icon_tab_dashboard
    )

    object Setting : MainNavTab(
        id = 2,
        nameResId = R.string.tab_setting,
        iconResId = R.drawable.icon_tab_setting,
    )
}