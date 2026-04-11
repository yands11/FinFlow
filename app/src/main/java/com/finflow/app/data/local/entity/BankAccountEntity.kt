package com.finflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_account")
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val bankName: String,
    val accountNumber: String,
    val ownerName: String,
    val description: String,
)
