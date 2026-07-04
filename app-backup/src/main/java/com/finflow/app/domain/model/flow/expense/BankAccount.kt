package com.finflow.app.domain.model.flow.expense

data class BankAccount(
    val id: Long,
    val name: String,
    val bankName: String,
    val accountNumber: String,
    val ownerName: String,
    val description: String
)