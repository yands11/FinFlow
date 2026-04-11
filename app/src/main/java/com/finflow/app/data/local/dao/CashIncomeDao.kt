package com.finflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finflow.app.data.local.entity.CashIncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashIncomeDao {

    @Query("SELECT * FROM cash_income WHERE year = :year AND month = :month ORDER BY id")
    fun getByMonth(year: Int, month: Int): Flow<List<CashIncomeEntity>>

    @Query("SELECT * FROM cash_income WHERE id = :id")
    suspend fun getById(id: Long): CashIncomeEntity?

    @Insert
    suspend fun insert(entity: CashIncomeEntity): Long

    @Update
    suspend fun update(entity: CashIncomeEntity)

    @Delete
    suspend fun delete(entity: CashIncomeEntity)

    @Query(
        """
        SELECT year, month, SUM(amount) AS totalIncome
        FROM cash_income
        GROUP BY year, month
        ORDER BY year, month
        """
    )
    fun getMonthlySummary(): Flow<List<MonthlyIncomeSummary>>
}

data class MonthlyIncomeSummary(
    val year: Int,
    val month: Int,
    val totalIncome: Long,
)
