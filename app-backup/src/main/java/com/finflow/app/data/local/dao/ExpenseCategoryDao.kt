package com.finflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finflow.app.data.local.entity.ExpenseCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseCategoryDao {

    @Query("SELECT * FROM expense_category ORDER BY id")
    fun getAll(): Flow<List<ExpenseCategoryEntity>>

    @Query("SELECT * FROM expense_category WHERE id = :id")
    suspend fun getById(id: Long): ExpenseCategoryEntity?

    @Insert
    suspend fun insert(entity: ExpenseCategoryEntity): Long

    @Update
    suspend fun update(entity: ExpenseCategoryEntity)

    @Delete
    suspend fun delete(entity: ExpenseCategoryEntity)
}
