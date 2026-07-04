package com.finflow.app.ui.composable.main.flow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = category.name,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
