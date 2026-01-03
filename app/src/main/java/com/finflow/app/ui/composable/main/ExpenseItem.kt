package com.finflow.app.ui.composable.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import java.text.DecimalFormat

@Composable
fun ExpenseItem(
    category: ExpenseCategory,
    name: String,
    amount: Long,
    bankAccountName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ExpenseCategoryTag(category = category)
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
            )
            Text(
                text = bankAccountName,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF454545),
            )
        }
        Text(
            text = "₩ ${DecimalFormat("#,###").format(amount)}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF111111),
        )
    }
}
