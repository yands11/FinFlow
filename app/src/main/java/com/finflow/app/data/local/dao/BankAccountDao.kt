package com.finflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finflow.app.data.local.entity.BankAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BankAccountDao {

    @Query("SELECT * FROM bank_account ORDER BY id")
    fun getAll(): Flow<List<BankAccountEntity>>

    @Query("SELECT * FROM bank_account WHERE id = :id")
    suspend fun getById(id: Long): BankAccountEntity?

    @Insert
    suspend fun insert(entity: BankAccountEntity): Long

    @Update
    suspend fun update(entity: BankAccountEntity)

    @Delete
    suspend fun delete(entity: BankAccountEntity)
}
