package com.finflow.app.data.repository

import com.finflow.app.data.local.dao.BankAccountDao
import com.finflow.app.data.local.dao.CashIncomeDao
import com.finflow.app.data.local.dao.ExpenseCategoryDao
import com.finflow.app.data.local.dao.ExpenseDao
import com.finflow.app.data.local.entity.BankAccountEntity
import com.finflow.app.data.local.entity.CashIncomeEntity
import com.finflow.app.data.local.entity.ExpenseCategoryEntity
import com.finflow.app.data.local.entity.ExpenseEntity
import com.finflow.app.data.local.entity.ExpenseType
import com.finflow.app.data.local.entity.ExpenseWithDetails
import com.finflow.app.domain.model.flow.CashIncome
import com.finflow.app.domain.model.flow.MonthFlow
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.Expense
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import com.finflow.app.ui.model.MonthStatUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.sorted

class FlowRepositoryImpl @Inject constructor(
    private val cashIncomeDao: CashIncomeDao,
    private val expenseDao: ExpenseDao,
    private val bankAccountDao: BankAccountDao,
    private val expenseCategoryDao: ExpenseCategoryDao,
) : FlowRepository {

    override fun getMonthFlow(year: Int, month: Int): Flow<MonthFlow> {
        val incomeFlow = cashIncomeDao.getByMonth(year, month)
        val generalFlow = expenseDao.getByMonthAndType(year, month, ExpenseType.GENERAL)
        val savingFlow = expenseDao.getByMonthAndType(year, month, ExpenseType.SAVING)
        val investmentFlow = expenseDao.getByMonthAndType(year, month, ExpenseType.INVESTMENT)

        return combine(incomeFlow, generalFlow, savingFlow, investmentFlow) { incomes, generals, savings, investments ->
            MonthFlow(
                incomeList = incomes.map { it.toDomain() },
                expenseList = generals.map { it.toGeneralDomain() },
                savingList = savings.map { it.toSavingDomain() },
                investmentList = investments.map { it.toInvestmentDomain() },
            )
        }
    }

    override fun getAllBankAccounts(): Flow<List<BankAccount>> {
        return bankAccountDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllCategories(): Flow<List<ExpenseCategory>> {
        return expenseCategoryDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getMonthlyStats(): Flow<List<MonthStatUiModel>> {
        val incomeFlow = cashIncomeDao.getMonthlySummary()
        val expenseFlow = expenseDao.getMonthlySummaryByType()

        return combine(incomeFlow, expenseFlow) { incomeSummaries, expenseSummaries ->
            val months = (incomeSummaries.map { it.year to it.month } +
                    expenseSummaries.map { it.year to it.month }).distinct().sortedBy { it.second }

            months.map { (year, month) ->
                val income = incomeSummaries
                    .firstOrNull { it.year == year && it.month == month }
                    ?.totalIncome ?: 0L

                val expenseByType = expenseSummaries
                    .filter { it.year == year && it.month == month }

                MonthStatUiModel(
                    monthLabel = "${month}월",
                    incomeTotal = income,
                    expenseTotal = expenseByType
                        .firstOrNull { it.type == ExpenseType.GENERAL }?.totalAmount ?: 0L,
                    savingTotal = expenseByType
                        .firstOrNull { it.type == ExpenseType.SAVING }?.totalAmount ?: 0L,
                    investmentTotal = expenseByType
                        .firstOrNull { it.type == ExpenseType.INVESTMENT }?.totalAmount ?: 0L,
                )
            }
        }
    }

    override suspend fun addIncome(title: String, amount: Long, year: Int, month: Int): Long {
        return cashIncomeDao.insert(
            CashIncomeEntity(title = title, amount = amount, year = year, month = month)
        )
    }

    override suspend fun addExpense(
        type: String,
        categoryId: Long,
        name: String,
        amount: Long,
        bankAccountId: Long,
        year: Int,
        month: Int,
    ): Long {
        return expenseDao.insert(
            ExpenseEntity(
                type = ExpenseType.valueOf(type),
                categoryId = categoryId,
                name = name,
                amount = amount,
                bankAccountId = bankAccountId,
                year = year,
                month = month,
            )
        )
    }

    override suspend fun addBankAccount(
        name: String,
        bankName: String,
        accountNumber: String,
        ownerName: String,
        description: String,
    ): Long {
        return bankAccountDao.insert(
            BankAccountEntity(
                name = name,
                bankName = bankName,
                accountNumber = accountNumber,
                ownerName = ownerName,
                description = description,
            )
        )
    }

    override suspend fun addCategory(name: String): Long {
        return expenseCategoryDao.insert(ExpenseCategoryEntity(name = name))
    }

    override suspend fun deleteIncome(id: Long) {
        cashIncomeDao.getById(id)?.let {
            cashIncomeDao.delete(it)
        }
    }

    override suspend fun deleteExpense(id: Long) {
        expenseDao.getById(id)?.let {
            expenseDao.delete(it)
        }
    }
}

// Entity → Domain 매퍼

private fun CashIncomeEntity.toDomain() = CashIncome(
    id = id,
    title = title,
    amount = amount,
)

private fun BankAccountEntity.toDomain() = BankAccount(
    id = id,
    name = name,
    bankName = bankName,
    accountNumber = accountNumber,
    ownerName = ownerName,
    description = description,
)

private fun ExpenseCategoryEntity.toDomain() = ExpenseCategory(
    id = id,
    name = name,
)

private val unknownCategory = ExpenseCategory(id = 0, name = "미분류")
private val unknownAccount = BankAccount(
    id = 0, name = "미지정", bankName = "", accountNumber = "", ownerName = "", description = ""
)

private fun ExpenseWithDetails.toGeneralDomain() = Expense.General(
    id = expense.id,
    category = category?.toDomain() ?: unknownCategory,
    name = expense.name,
    amount = expense.amount,
    bankAccount = bankAccount?.toDomain() ?: unknownAccount,
)

private fun ExpenseWithDetails.toSavingDomain() = Expense.Saving(
    id = expense.id,
    category = category?.toDomain() ?: unknownCategory,
    name = expense.name,
    amount = expense.amount,
    bankAccount = bankAccount?.toDomain() ?: unknownAccount,
)

private fun ExpenseWithDetails.toInvestmentDomain() = Expense.Investment(
    id = expense.id,
    category = category?.toDomain() ?: unknownCategory,
    name = expense.name,
    amount = expense.amount,
    bankAccount = bankAccount?.toDomain() ?: unknownAccount,
)
