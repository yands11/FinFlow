package com.finflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.finflow.app.data.local.entity.ExpenseEntity
import com.finflow.app.data.local.entity.ExpenseType
import com.finflow.app.data.local.entity.ExpenseWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Transaction
    @Query(
        """
        SELECT * FROM expense
        WHERE year = :year AND month = :month AND type = :type
        ORDER BY id
        """
    )
    fun getByMonthAndType(year: Int, month: Int, type: ExpenseType): Flow<List<ExpenseWithDetails>>

    @Transaction
    @Query("SELECT * FROM expense WHERE year = :year AND month = :month ORDER BY id")
    fun getByMonth(year: Int, month: Int): Flow<List<ExpenseWithDetails>>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @Insert
    suspend fun insert(entity: ExpenseEntity): Long

    @Update
    suspend fun update(entity: ExpenseEntity)

    @Delete
    suspend fun delete(entity: ExpenseEntity)

    @Query(
        """
        SELECT year, month, type, SUM(amount) AS totalAmount
        FROM expense
        GROUP BY year, month, type
        ORDER BY year, month
        """
    )
    fun getMonthlySummaryByType(): Flow<List<MonthlyExpenseSummary>>

    @Query("SELECT DISTINCT year * 100 + month AS monthKey FROM expense ORDER BY monthKey")
    fun getAvailableMonthKeys(): Flow<List<Int>>

    @Query("SELECT * FROM expense WHERE year = :year AND month = :month")
    suspend fun getByMonthOnce(year: Int, month: Int): List<ExpenseEntity>
}

data class MonthlyExpenseSummary(
    val year: Int,
    val month: Int,
    val type: ExpenseType,
    val totalAmount: Long,
)
