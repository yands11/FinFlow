package com.finflow.app.data.repository

import com.finflow.app.domain.model.flow.CashIncome
import com.finflow.app.domain.model.flow.MonthFlow
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import com.finflow.app.ui.model.MonthStatUiModel
import kotlinx.coroutines.flow.Flow

interface FlowRepository {

    fun getMonthFlow(year: Int, month: Int): Flow<MonthFlow>

    fun getAllBankAccounts(): Flow<List<BankAccount>>

    fun getAllCategories(): Flow<List<ExpenseCategory>>

    fun getMonthlyStats(): Flow<List<MonthStatUiModel>>

    suspend fun addIncome(title: String, amount: Long, year: Int, month: Int): Long

    suspend fun addExpense(
        type: String,
        categoryId: Long,
        name: String,
        amount: Long,
        bankAccountId: Long,
        year: Int,
        month: Int,
    ): Long

    suspend fun addBankAccount(
        name: String,
        bankName: String,
        accountNumber: String,
        ownerName: String,
        description: String,
    ): Long

    suspend fun addCategory(name: String): Long

    suspend fun deleteIncome(id: Long)

    suspend fun deleteExpense(id: Long)
}
