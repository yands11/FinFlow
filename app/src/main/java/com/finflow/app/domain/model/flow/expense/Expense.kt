package com.finflow.app.domain.model.flow.expense

sealed interface Expense {

    val amount: Long
    val bankAccount: BankAccount

    data class General(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense

    data class Investment(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense

    data class Saving(
        val id: Long,
        val category: ExpenseCategory,
        val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense
}