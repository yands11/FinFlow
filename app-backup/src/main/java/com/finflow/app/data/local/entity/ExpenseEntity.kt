package com.finflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expense",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["bankAccountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("categoryId"),
        Index("bankAccountId"),
        Index("year", "month"),
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ExpenseType,
    val categoryId: Long?,
    val name: String,
    val amount: Long,
    val bankAccountId: Long?,
    val year: Int,
    val month: Int,
)
