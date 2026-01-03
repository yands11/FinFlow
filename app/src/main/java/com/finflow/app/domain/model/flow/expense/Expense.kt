package com.finflow.app.domain.model.flow.expense

sealed interface Expense {

    data class General(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        val amount: Long,
        val bankAccount: BankAccount
    ) : Expense

    data class Investment(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        val amount: Long,
        val bankAccount: BankAccount
    ) : Expense

    data class Saving(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        val amount: Long,
        val bankAccount: BankAccount
    ) : Expense
}