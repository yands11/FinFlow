package com.finflow.app.ui.composable.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.finflow.app.R
import com.finflow.app.ui.model.ExpenseRateBarUiModel


@Composable
fun ExpenseRateBar(
    uiModel: ExpenseRateBarUiModel,
    modifier: Modifier = Modifier
) {

    val cashColor = Color(0xFFF9989F)
    val savingColor = Color(0xFFFAF096)
    val investmentColor = Color(0xFFC5F8C8)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            if (uiModel.cashRatio > 0) {
                Box(
                    modifier = Modifier
                        .weight(uiModel.cashRatio)
                        .fillMaxHeight()
                        .background(cashColor)
                )
            }

            if (uiModel.savingRatio > 0) {
                Box(
                    modifier = Modifier
                        .weight(uiModel.savingRatio)
                        .fillMaxHeight()
                        .background(savingColor)
                )
            }

            if (uiModel.investmentRatio > 0) {
                Box(
                    modifier = Modifier
                        .weight(uiModel.investmentRatio)
                        .fillMaxHeight()
                        .background(investmentColor)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun ExpenseRateBarPreview() {
    ExpenseRateBar(
        uiModel = ExpenseRateBarUiModel(
            cashRatio = 0.7F,
            savingRatio = 0.2F,
            investmentRatio = 0.1F
        ),
        modifier = Modifier.fillMaxWidth()
    )
}