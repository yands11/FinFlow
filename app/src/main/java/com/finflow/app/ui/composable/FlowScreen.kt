package com.finflow.app.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflow.app.R
import com.finflow.app.ui.model.IncomeUiModel
import java.text.DecimalFormat

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SectionTotalAmount(
    amount: Long,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "₩ ${DecimalFormat("#,###").format(amount)}",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun IncomeItem(
    income: IncomeUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = income.title,
            fontSize = 12.sp,
            modifier = Modifier.weight(1F)
        )
        Text(
            text = "₩ ${DecimalFormat("#,###").format(income.amount)}",
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun IncomeList(
    incomeList: List<IncomeUiModel>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items(count = incomeList.size) { index ->
            IncomeItem(income = incomeList[index])
        }
    }
}

@Composable
fun IncomeCard(
    title: String,
    totalAmount: Long,
    incomeList: List<IncomeUiModel>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.income_section_bg_color)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 16.dp)
        ) {
            SectionTitle(title = title)
            SectionTotalAmount(amount = totalAmount)
            IncomeList(
                incomeList = incomeList,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionTitlePreview() {
    SectionTitle(
        title = "Income"
    )
}

@Preview(showBackground = true)
@Composable
fun SectionTotalAmountPreview() {
    SectionTotalAmount(
        amount = 999_999_999_999L
    )
}

@Preview(showBackground = true)
@Composable
fun IncomeItemPreview() {
    IncomeItem(income = IncomeUiModel("월급", 5000000L))
}

@Preview(
    showBackground = true,
    widthDp = 300,
)
@Composable
fun IncomeCardPreview() {
    IncomeCard(
        title = "Income",
        totalAmount = 6000000L,
        incomeList = listOf(
            IncomeUiModel("월급", 5000000L),
            IncomeUiModel("인센티브", 1000000L)
        )
    )
}
