package com.finflow.app.domain.model.flow.expense

sealed interface Expense {

    val id: Long
    val category: ExpenseCategory
    val name: String
    val amount: Long
    val bankAccount: BankAccount

    data class General(
        override val id: Long,
        override val category: ExpenseCategory,
        override val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense

    data class Investment(
        override val id: Long,
        override val category: ExpenseCategory,
        override val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense

    data class Saving(
        override val id: Long,
        override val category: ExpenseCategory,
        override val name: String,
        override val amount: Long,
        override val bankAccount: BankAccount,
    ) : Expense
}
