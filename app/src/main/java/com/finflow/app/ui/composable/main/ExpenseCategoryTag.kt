package com.finflow.app.ui.composable.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.finflow.app.domain.model.flow.expense.ExpenseCategory

@Composable
fun ExpenseCategoryTag(
    category: ExpenseCategory,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFF2898D9),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = category.name,
            color = Color(0xFFFEFEFE),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview
@Composable
fun ExpenseCategoryTagPreview() {
    ExpenseCategoryTag(
        category = ExpenseCategory(
            id = 1,
            name = "생활비"
        )
    )
}