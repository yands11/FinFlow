package com.finflow.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithDetails(
    @Embedded
    val expense: ExpenseEntity,
    @Relation(parentColumn = "categoryId", entityColumn = "id")
    val category: ExpenseCategoryEntity?,
    @Relation(parentColumn = "bankAccountId", entityColumn = "id")
    val bankAccount: BankAccountEntity?,
)
