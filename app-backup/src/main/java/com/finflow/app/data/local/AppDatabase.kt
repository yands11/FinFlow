package com.finflow.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finflow.app.data.local.dao.BankAccountDao
import com.finflow.app.data.local.dao.CashIncomeDao
import com.finflow.app.data.local.dao.ExpenseCategoryDao
import com.finflow.app.data.local.dao.ExpenseDao
import com.finflow.app.data.local.entity.BankAccountEntity
import com.finflow.app.data.local.entity.CashIncomeEntity
import com.finflow.app.data.local.entity.ExpenseCategoryEntity
import com.finflow.app.data.local.entity.ExpenseEntity

@Database(
    entities = [
        BankAccountEntity::class,
        ExpenseCategoryEntity::class,
        CashIncomeEntity::class,
        ExpenseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun expenseCategoryDao(): ExpenseCategoryDao
    abstract fun cashIncomeDao(): CashIncomeDao
    abstract fun expenseDao(): ExpenseDao
}
